# S.A.M. AI & Android IDE — Unified UI Suite

All user interface screens for the **S.A.M. (Smart Autonomous Machine) AI & Android IDE** have been consolidated into a clean, unified single directory: `ui/`.

---

## 🚀 Quick Access

- **Master Interactive Showcase Hub:** Open [`ui/index.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/index.html) in any browser.
- **Standalone Screen Files:** Located in [`ui/screens/`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/).

---

## 📱 Complete Screen Directory (19 Screens)

| # | Screen Name | File Path | Category | Description |
|---|-------------|-----------|----------|-------------|
| **01** | **Home Dashboard** | [`01_home_dashboard.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/01_home_dashboard.html) | Core | Autonomous workspace dashboard with central AI core pulse & live telemetry |
| **02** | **Home Dashboard (v1)** | [`02_home_dashboard_v1.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/02_home_dashboard_v1.html) | Core | Compact high-density mobile layout with telemetry cards |
| **03** | **Home Dashboard (v2)** | [`03_home_dashboard_v2.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/03_home_dashboard_v2.html) | Core | Interactive simulation controls (AI Busy, Offline Cache, Empty state) |
| **04** | **AI IDE Workspace** | [`04_ai_ide_workspace.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/04_ai_ide_workspace.html) | IDE | Full file explorer tree, tabbed editor, build output & Git sync status |
| **05** | **Code Editor** | [`05_code_editor.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/05_code_editor.html) | IDE | High-mobility editor with Rust/Kotlin syntax highlighting and AST analysis |
| **06** | **AI Chat** | [`06_ai_chat.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/06_ai_chat.html) | AI | Real-time AI chat pairing with code block generation and live execution |
| **07** | **AI Chat (Dedicated)** | [`07_ai_chat_dedicated.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/07_ai_chat_dedicated.html) | AI | Dedicated chat viewport with slide-up model selector (Claude, GPT, Ollama) |
| **08** | **AI Chat (Refined)** | [`08_ai_chat_refined.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/08_ai_chat_refined.html) | AI | Context-aware repository breadcrumb bar and interactive prompt composer |
| **09** | **Agent Mode** | [`09_agent_mode.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/09_agent_mode.html) | AI | Multi-file autonomous refactoring, AST verification, test suite execution |
| **10** | **Voice Assistant** | [`10_voice_assistant.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/10_voice_assistant.html) | AI | JARVIS-style voice persona, Marathi & English speech processing waveform |
| **11** | **Live Preview** | [`11_live_preview.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/11_live_preview.html) | IDE | Live application rendering with interactive runtime console & inspection |
| **12** | **Memory Core** | [`12_memory_core.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/12_memory_core.html) | AI | Vector context database, project memory retention & rule constraint engine |
| **13** | **AI Providers & Models** | [`13_ai_providers_models.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/13_ai_providers_models.html) | System | API key vault (Anthropic, OpenAI, Google DeepMind, Local Ollama) |
| **14** | **Projects Manager** | [`14_projects.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/14_projects.html) | Projects | Active repository explorer, branch indicators, language tags & filters |
| **15** | **New Project Wizard** | [`15_new_project.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/15_new_project.html) | Projects | Project scaffolding wizard for Kotlin/Android, Python, React Native & Web |
| **16** | **Permission Setup** | [`16_permission_setup.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/16_permission_setup.html) | System | Android API 34 security clearance (Microphone, Overlay, Accessibility) |
| **17** | **Onboarding & Boot** | [`17_onboarding_developer_credit.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/17_onboarding_developer_credit.html) | System | System initialization screen with official **"DEVELOPED BY SHLOK"** badge |
| **18** | **Developer Profile** | [`18_developer_profile.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/18_developer_profile.html) | System | Creator profile for Shlok Sathe (CSE Data Science Engineering) |
| **19** | **Settings** | [`19_settings.html`](file:///e:/SAM%20AI%20ANDODE%20IDE/ui/screens/19_settings.html) | System | Global preferences, AI personas, memory limits, and Android automation |

---

## 🎨 Design System Specifications

- **Aesthetic:** Dark Cybernetic Studio / Architectural Zero-Glow Precision.
- **Color Palette:**
  - Background: `#07090E` / `#090B10`
  - Cards & Containers: `#11151D` / `#171D28` / `#1E2535`
  - Hairline Borders: `#1E2638` / `#232B3B`
  - Primary Accent: `#5685FF` (Electric Cobalt)
  - Secondary Accent: `#9B72FF` (Cyber Violet)
  - Tertiary Accent: `#42D6A4` (Mint Emerald)
- **Typography:**
  - Body & Headlines: `Inter`
  - Code & Telemetry: `JetBrains Mono`
- **Iconography:** Google Material Symbols Outlined

---

## ⚡ Master Hub Features (`ui/index.html`)

1. **Instant Search & Filter:** Search by screen title, filename, or category.
2. **Multi-Viewport Device Frame Switcher:**
   - `390px`: Mobile Standard (Compact)
   - `440px`: Android Handheld Chassis
   - `768px`: Tablet / Foldable Expanded
   - `Full`: Responsive Fullscreen
3. **Hotkeys:**
   - `↑` / `↓`: Navigate to Previous / Next screen
   - `1`, `2`, `3`, `4` / `F`: Change viewport size
4. **Cross-Screen Navigation:** All internal links between screens are wired and functional.
