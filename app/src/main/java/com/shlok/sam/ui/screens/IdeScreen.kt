package com.shlok.sam.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shlok.sam.data.model.ProjectFile
import com.shlok.sam.ui.theme.*
import com.shlok.sam.viewmodel.IdeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeScreen(
    viewModel: IdeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToLivePreview: () -> Unit
) {
    val context = LocalContext.current
    val currentProject by viewModel.currentProject.collectAsState()
    val fileTree by viewModel.fileTree.collectAsState()
    val activeFile by viewModel.activeFile.collectAsState()
    val editorContent by viewModel.editorContent.collectAsState()
    val isBuilding by viewModel.isBuilding.collectAsState()
    val buildLogs by viewModel.buildLogs.collectAsState()

    var showBuildOutput by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = currentProject?.name ?: "S.A.M. IDE WORKSPACE",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextOnSurface
                        )
                        Text(
                            text = activeFile?.name ?: "No file selected",
                            style = MaterialTheme.typography.labelSmall,
                            color = CobaltPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextOnSurface)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveCurrentFile()
                        Toast.makeText(context, "File saved", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "Save", tint = TextOnSurface)
                    }
                    IconButton(onClick = {
                        showBuildOutput = true
                        viewModel.runBuild()
                    }) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Build & Run", tint = MintEmerald)
                    }
                    IconButton(onClick = onNavigateToLivePreview) {
                        Icon(imageVector = Icons.Outlined.Preview, contentDescription = "Live Preview", tint = CyberViolet)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Horizontal Split: Top File Tab Bar, Middle Editor, Bottom Terminal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Description, contentDescription = null, tint = CobaltPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = activeFile?.name ?: "Untitled",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextOnSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${editorContent.lines().size} lines",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            // Code Editor Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(DarkSurfaceLowest)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Line numbers gutter
                    val lineCount = editorContent.lines().size.coerceAtLeast(1)
                    LazyColumn(
                        modifier = Modifier
                            .width(36.dp)
                            .fillMaxHeight()
                            .background(DarkSurface)
                            .border(BorderStroke(1.dp, HairlineBorder))
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items((1..lineCount).toList()) { lineNum ->
                            Text(
                                text = "$lineNum",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Editable text field
                    OutlinedTextField(
                        value = editorContent,
                        onValueChange = { viewModel.updateEditorContent(it) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkSurfaceLowest,
                            unfocusedContainerColor = DarkSurfaceLowest,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = PrimaryFixed,
                            unfocusedTextColor = PrimaryFixed
                        ),
                        textStyle = MaterialTheme.typography.labelMedium
                    )
                }
            }

            // Build output / Terminal drawer
            if (showBuildOutput || isBuilding) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, HairlineBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp,
                                    color = if (isBuilding) CobaltPrimary else MintEmerald
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBuilding) "GRADLE BUILD IN PROGRESS..." else "BUILD TERMINAL (READY)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isBuilding) CobaltPrimary else MintEmerald
                                )
                            }
                            IconButton(onClick = { showBuildOutput = false }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(DarkSurfaceLowest, RoundedCornerShape(6.dp))
                                .padding(8.dp)
                        ) {
                            items(buildLogs) { log ->
                                Text(
                                    text = log,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (log.contains("BUILD SUCCESSFUL")) MintEmerald else TextOnSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
