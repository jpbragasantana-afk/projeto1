# 🖥️ Java System Monitor

A comprehensive real-time system monitoring application built with Java Swing that provides detailed insights into your computer's performance and resource usage.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)
![Platform](https://img.shields.io/badge/Platform-Cross--Platform-lightgrey?style=for-the-badge)

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Screenshots](#screenshots)
- [Installation](#installation)
- [Usage](#usage)
- [System Requirements](#system-requirements)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [License](#license)
- [Support](#support)

## 🎯 Overview

The Java System Monitor is a powerful desktop application that provides real-time monitoring of system resources and performance metrics. Built with Java Swing, it offers a modern dark-themed interface with multiple tabs for different system components.

### Key Highlights

- **Real-time Monitoring**: Live updates every second for all system metrics
- **Multi-tab Interface**: Organized view of different system components
- **Modern UI**: Dark theme with color-coded metrics and progress bars
- **Cross-platform**: Runs on Windows, macOS, and Linux
- **Lightweight**: Minimal resource footprint while providing comprehensive monitoring

## ✨ Features

### 📊 Dashboard
- **Overview Panel**: Quick glance at all critical system metrics
- **Color-coded Cards**: Visual representation of CPU, Memory, Disk, Network usage
- **Real-time Updates**: Live data refresh every second
- **System Uptime**: Track how long your system has been running

### 🔧 CPU Monitoring
- **Processor Information**: Detailed CPU specifications and capabilities
- **Usage Statistics**: Real-time CPU utilization monitoring
- **Core Information**: Individual core performance tracking
- **System Load**: Overall system performance metrics

### 💾 Memory Management
- **RAM Usage**: Real-time memory consumption tracking
- **Java Heap**: JVM memory usage and garbage collection stats
- **Memory Breakdown**: Used, free, and total memory display
- **Progress Bars**: Visual representation of memory usage

### 💿 Disk Storage
- **Drive Information**: All mounted drives and their details
- **Storage Usage**: Used and available space for each drive
- **Usage Percentage**: Visual indicators of disk utilization
- **Path Information**: Complete drive paths and mount points

### 🌐 Network Monitoring
- **Interface Detection**: All active network interfaces
- **MAC Addresses**: Hardware addresses for network adapters
- **Transfer Statistics**: Upload and download speeds
- **Packet Counters**: Network traffic monitoring

### ⚙️ Process Management
- **Active Processes**: List of currently running processes
- **Process Details**: PID, name, status, and resource usage
- **Thread Monitoring**: Active thread count tracking
- **Performance Metrics**: CPU and memory usage per process

### 🖥️ System Information
- **Operating System**: Detailed OS information and version
- **Java Runtime**: JVM details and configuration
- **User Environment**: Current user and directory information
- **Hardware Specs**: Processor count and system capabilities

## 🖼️ Screenshots

### Dashboard View
The main dashboard provides a comprehensive overview of all system metrics in an easy-to-read format with color-coded cards for quick status assessment.

### CPU Monitoring
Detailed CPU information including specifications, real-time usage statistics, and performance metrics for system optimization.

### Memory Usage
Real-time memory monitoring with both system RAM and Java heap memory tracking, including visual progress bars.

## 🚀 Installation

### Prerequisites

- **Java Development Kit (JDK) 8 or higher**
- **Operating System**: Windows, macOS, or Linux

### Quick Start

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/java-system-monitor.git
   cd java-system-monitor
   ```

2. **Compile the Application**
   ```bash
   javac monitor.java
   ```

3. **Run the Monitor**
   ```bash
   java MonitorFinal
   ```

### Alternative Installation Methods

#### Using JAR File
```bash
# Compile to JAR
jar cfe SystemMonitor.jar MonitorFinal monitor.java

# Run JAR
java -jar SystemMonitor.jar
```

#### IDE Setup
1. Import the project into your favorite Java IDE (IntelliJ IDEA, Eclipse, NetBeans)
2. Ensure JDK 8+ is configured
3. Run the `MonitorFinal` class

## 📖 Usage

### Starting the Application

Launch the system monitor by running the compiled Java class:

```bash
java MonitorFinal
```

### Navigation

The application features a tabbed interface with the following sections:

- **Dashboard**: Overview of all system metrics
- **CPU**: Processor information and usage statistics
- **Memoria**: RAM and Java heap memory monitoring
- **Disco**: Disk storage information and usage
- **Rede**: Network interfaces and traffic statistics
- **Processos**: Running processes and thread information
- **Sistema**: Detailed system and Java runtime information

### Real-time Updates

All metrics are automatically updated every second. The application uses Java's Timer class to ensure consistent and accurate real-time monitoring.

### Customization

The application supports various customization options:

- **Update Interval**: Modify the timer interval in the code
- **Color Themes**: Adjust colors in the UI creation methods
- **Metric Selection**: Enable/disable specific monitoring features

## 🔧 System Requirements

### Minimum Requirements

- **Java**: JDK/JRE 8 or higher
- **RAM**: 256 MB available memory
- **Storage**: 50 MB free disk space
- **Display**: 1024x768 resolution minimum

### Recommended Requirements

- **Java**: JDK/JRE 11 or higher
- **RAM**: 512 MB available memory
- **Storage**: 100 MB free disk space
- **Display**: 1400x900 resolution or higher

### Supported Platforms

- ✅ **Windows** (7, 8, 10, 11)
- ✅ **macOS** (10.10+)
- ✅ **Linux** (Ubuntu, CentOS, Debian, etc.)

## 🏗️ Architecture

### Class Structure

```
MonitorFinal
├── UI Components
│   ├── JFrame (Main Window)
│   ├── JTabbedPane (Tab Container)
│   └── Custom Panels (Dashboard, CPU, Memory, etc.)
├── Data Collection
│   ├── System Properties
│   ├── Runtime Statistics
│   └── Management Beans
└── Real-time Updates
    ├── Timer (1-second intervals)
    └── Event Handlers
```

### Key Components

#### Main Class: `MonitorFinal`
- **Purpose**: Entry point and main controller
- **Responsibilities**: UI initialization, data coordination, timer management

#### UI Creation Methods
- `createDashboardTab()`: Overview panel with metric cards
- `createCPUTab()`: CPU information and usage display
- `createMemoryTab()`: RAM and Java heap monitoring
- `createDiskTab()`: Storage information table
- `createNetworkTab()`: Network interface details
- `createProcessTab()`: Process list and statistics
- `createSystemTab()`: System information display

#### Data Update Methods
- `updateCPUData()`: CPU usage and information
- `updateMemoryData()`: Memory statistics
- `updateDiskData()`: Storage information
- `updateNetworkData()`: Network statistics
- `updateProcessData()`: Process information
- `updateSystemData()`: System details

### Design Patterns

- **Observer Pattern**: Timer-based updates for real-time monitoring
- **Factory Pattern**: UI component creation methods
- **Singleton Pattern**: Single application instance management

## 🛠️ Development

### Building from Source

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/java-system-monitor.git
   ```

2. **Navigate to project directory**
   ```bash
   cd java-system-monitor
   ```

3. **Compile the source**
   ```bash
   javac -cp . monitor.java
   ```

4. **Run the application**
   ```bash
   java MonitorFinal
   ```

### Code Structure

The application follows a modular design with clear separation of concerns:

- **UI Layer**: Swing components and layout management
- **Data Layer**: System information collection and processing
- **Update Layer**: Real-time data refresh and display updates

### Extending the Monitor

To add new monitoring features:

1. Create a new tab method (e.g., `createGPUTab()`)
2. Add corresponding update method (e.g., `updateGPUData()`)
3. Register components in the `valueComponents` map
4. Add timer update call in `setupMonitoring()`

## 🤝 Contributing

We welcome contributions to improve the Java System Monitor! Here's how you can help:

### Ways to Contribute

- 🐛 **Bug Reports**: Report issues and bugs
- 💡 **Feature Requests**: Suggest new monitoring capabilities
- 🔧 **Code Contributions**: Submit pull requests with improvements
- 📚 **Documentation**: Improve documentation and examples
- 🎨 **UI/UX**: Enhance the user interface and experience

### Development Guidelines

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/new-monitoring-feature
   ```
3. **Make your changes**
4. **Test thoroughly**
5. **Submit a pull request**

### Code Standards

- Follow Java naming conventions
- Add comments for complex logic
- Ensure cross-platform compatibility
- Test on multiple operating systems
- Maintain the existing code style

### Pull Request Process

1. Ensure your code compiles without warnings
2. Test on at least two different platforms
3. Update documentation if needed
4. Describe your changes in the PR description
5. Link any related issues

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 Java System Monitor

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 🆘 Support

### Getting Help

- 📖 **Documentation**: Check this README and code comments
- 🐛 **Issues**: Report bugs on GitHub Issues
- 💬 **Discussions**: Join GitHub Discussions for questions
- 📧 **Email**: Contact the maintainers directly

### Troubleshooting

#### Common Issues

**Application won't start**
- Ensure Java 8+ is installed: `java -version`
- Check classpath and compilation: `javac monitor.java`

**UI appears corrupted**
- Update to latest Java version
- Check display resolution and scaling settings

**Performance issues**
- Increase JVM heap size: `java -Xmx512m MonitorFinal`
- Close other resource-intensive applications

**Network information not showing**
- Run with administrator/root privileges if needed
- Check network interface permissions

### System-Specific Notes

#### Windows
- May require administrator privileges for some system information
- Windows Defender might flag the application initially

#### macOS
- Grant necessary permissions in System Preferences > Security & Privacy
- Some features may require developer tools installation

#### Linux
- Ensure proper permissions for system file access
- Install required system libraries if missing

## 🔮 Future Enhancements

### Planned Features

- 📊 **Historical Data**: Charts and graphs for performance trends
- 🔔 **Alerts**: Configurable thresholds and notifications
- 📁 **Export**: Save monitoring data to files
- 🌐 **Web Interface**: Browser-based monitoring dashboard
- 🔌 **Plugins**: Extensible architecture for custom monitors
- 📱 **Mobile App**: Companion mobile application
- 🖥️ **GPU Monitoring**: Graphics card performance tracking
- 🌡️ **Temperature**: Hardware temperature monitoring

### Roadmap

- **v2.0**: Historical data and charting
- **v2.1**: Alert system and notifications
- **v2.2**: Export and reporting features
- **v3.0**: Web interface and remote monitoring

## 🙏 Acknowledgments

- **Java Community**: For the robust platform and libraries
- **Swing Framework**: For the GUI components and layout managers
- **Open Source Contributors**: For inspiration and best practices
- **System Monitoring Tools**: For reference implementations and ideas

---

**⭐ If you find this project useful, please consider giving it a star on GitHub!**
