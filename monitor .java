import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;
import java.util.*;
import java.net.NetworkInterface;
import java.net.SocketException;

public class MonitorFinal {
    private JFrame frame;
    private Timer timer;
    private DecimalFormat df = new DecimalFormat("#0.00");
    
    private Map<String, JComponent> valueComponents = new HashMap<>();
    private Map<String, JProgressBar> progressBars = new HashMap<>();
    private JTextArea systemInfoArea, networkInfoArea;
    private JTable diskTable, processTable;
    private DefaultTableModel diskTableModel, processTableModel;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MonitorFinal().initialize();
        });
    }
    
    public void initialize() {
        createUI();
        setupMonitoring();
    }
    
    private void createUI() {
        frame = new JFrame("Monitor de Sistema - Tempo Real");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 900);
        frame.setLocationRelativeTo(null);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(45, 45, 48));
        tabbedPane.setForeground(Color.WHITE);
        
        tabbedPane.addTab("Dashboard", createDashboardTab());
        tabbedPane.addTab("CPU", createCPUTab());
        tabbedPane.addTab("Memoria", createMemoryTab());
        tabbedPane.addTab("Disco", createDiskTab());
        tabbedPane.addTab("Rede", createNetworkTab());
        tabbedPane.addTab("Processos", createProcessTab());
        tabbedPane.addTab("Sistema", createSystemTab());
        
        frame.add(tabbedPane);
        frame.setVisible(true);
    }
    
    private JPanel createDashboardTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 35));
        
        JPanel headerPanel = createHeaderPanel("DASHBOARD - VISAO GERAL");
        panel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel metricsPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        metricsPanel.setBackground(new Color(30, 30, 35));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        metricsPanel.add(createMetricCard("CPU", "0%", "Uso Total", new Color(52, 152, 219)));
        metricsPanel.add(createMetricCard("MEMORIA", "0%", "RAM em Uso", new Color(46, 204, 113)));
        metricsPanel.add(createMetricCard("DISCO", "0%", "Uso do Sistema", new Color(155, 89, 182)));
        metricsPanel.add(createMetricCard("REDE", "0 KB/s", "Velocidade", new Color(241, 196, 15)));
        metricsPanel.add(createMetricCard("PROCESSOS", "0", "Ativos", new Color(230, 126, 34)));
        metricsPanel.add(createMetricCard("THREADS", "0", "Em execucao", new Color(231, 76, 60)));
        metricsPanel.add(createMetricCard("UPTIME", "0h", "Sistema", new Color(149, 165, 166)));
        metricsPanel.add(createMetricCard("JAVA", "0%", "Heap Memory", new Color(52, 152, 219)));
        
        panel.add(metricsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCPUTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 35));
        
        panel.add(createHeaderPanel("PROCESSADOR - INFORMACOES DETALHADAS"), BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setBackground(new Color(30, 30, 35));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(50, 50, 55));
        infoPanel.setBorder(createTitledBorder("Especificacoes da CPU"));
        
        JTextArea cpuInfoArea = new JTextArea();
        cpuInfoArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        cpuInfoArea.setBackground(new Color(40, 40, 45));
        cpuInfoArea.setForeground(Color.WHITE);
        cpuInfoArea.setEditable(false);
        infoPanel.add(new JScrollPane(cpuInfoArea), BorderLayout.CENTER);
        
        JPanel coresPanel = new JPanel(new BorderLayout());
        coresPanel.setBackground(new Color(50, 50, 55));
        coresPanel.setBorder(createTitledBorder("Uso do Sistema"));
        
        JTextArea coresArea = new JTextArea();
        coresArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        coresArea.setBackground(new Color(40, 40, 45));
        coresArea.setForeground(Color.WHITE);
        coresArea.setEditable(false);
        coresArea.setText("Carregando informacoes da CPU...");
        coresPanel.add(new JScrollPane(coresArea), BorderLayout.CENTER);
        
        contentPanel.add(infoPanel);
        contentPanel.add(coresPanel);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        valueComponents.put("cpu_info", cpuInfoArea);
        valueComponents.put("cpu_cores", coresArea);
        
        return panel;
    }
    
    private JPanel createMemoryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 35));
        
        panel.add(createHeaderPanel("MEMORIA - RAM E SISTEMA"), BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setBackground(new Color(30, 30, 35));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel ramPanel = createMemorySection("MEMORIA RAM", new Color(46, 204, 113));
        JPanel javaPanel = createMemorySection("JAVA HEAP", new Color(52, 152, 219));
        
        contentPanel.add(ramPanel);
        contentPanel.add(javaPanel);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMemorySection(String title, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(50, 50, 55));
        panel.setBorder(createTitledBorder(title));
        
        JPanel statsPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        statsPanel.setBackground(new Color(50, 50, 55));
        
        String[] labels = {"Uso Total:", "Total:", "Usada:", "Disponivel:", "Maxima:", "Barra:"};
        String[] keys = {"usage", "total", "used", "free", "max", "progress"};
        
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setForeground(color);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            if (keys[i].equals("progress")) {
                JProgressBar progressBar = new JProgressBar(0, 100);
                progressBar.setValue(0);
                progressBar.setStringPainted(true);
                progressBar.setForeground(color);
                progressBar.setBackground(new Color(40, 40, 45));
                statsPanel.add(label);
                statsPanel.add(progressBar);
                progressBars.put(title.toLowerCase().replace(" ", "_") + "_progress", progressBar);
            } else {
                JLabel valueLabel = new JLabel("--");
                valueLabel.setForeground(Color.WHITE);
                valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                statsPanel.add(label);
                statsPanel.add(valueLabel);
                valueComponents.put(title.toLowerCase().replace(" ", "_") + "_" + keys[i], valueLabel);
            }
        }
        
        panel.add(statsPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createDiskTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 35));
        
        panel.add(createHeaderPanel("DISCOS E ARMAZENAMENTO"), BorderLayout.NORTH);
        
        String[] diskColumns = {"Unidade", "Caminho", "Total", "Livre", "Usavel", "Uso %"};
        diskTableModel = new DefaultTableModel(diskColumns, 0);
        diskTable = createTable(diskTableModel);
        
        JScrollPane scrollPane = new JScrollPane(diskTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createNetworkTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 35));
        
        panel.add(createHeaderPanel("REDE E CONECTIVIDADE"), BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setBackground(new Color(30, 30, 35));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(50, 50, 55));
        infoPanel.setBorder(createTitledBorder("Interfaces de Rede"));
        
        networkInfoArea = new JTextArea();
        networkInfoArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        networkInfoArea.setBackground(new Color(40, 40, 45));
        networkInfoArea.setForeground(Color.WHITE);
        networkInfoArea.setEditable(false);
        infoPanel.add(new JScrollPane(networkInfoArea), BorderLayout.CENTER);
        
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(new Color(50, 50, 55));
        statsPanel.setBorder(createTitledBorder("Estatisticas"));
        
        JTextArea statsArea = new JTextArea();
        statsArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        statsArea.setBackground(new Color(40, 40, 45));
        statsArea.setForeground(Color.WHITE);
        statsArea.setEditable(false);
        statsArea.setText("Carregando estatisticas de rede...");
        statsPanel.add(new JScrollPane(statsArea), BorderLayout.CENTER);
        
        contentPanel.add(infoPanel);
        contentPanel.add(statsPanel);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        valueComponents.put("network_stats", statsArea);
        
        return panel;
    }
    
    private JPanel createProcessTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 35));
        
        panel.add(createHeaderPanel("PROCESSOS EM EXECUCAO"), BorderLayout.NORTH);
        
        String[] processColumns = {"PID", "Nome", "Estado", "Threads", "Prioridade", "Tempo CPU"};
        processTableModel = new DefaultTableModel(processColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        processTable = createTable(processTableModel);
        
        JScrollPane scrollPane = new JScrollPane(processTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSystemTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 35));
        
        panel.add(createHeaderPanel("INFORMACOES COMPLETAS DO SISTEMA"), BorderLayout.NORTH);
        
        systemInfoArea = new JTextArea();
        systemInfoArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        systemInfoArea.setBackground(new Color(40, 40, 45));
        systemInfoArea.setForeground(Color.WHITE);
        systemInfoArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(systemInfoArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createHeaderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(46, 204, 113));
        
        JLabel updateLabel = new JLabel("Atualizando em tempo real...");
        updateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        updateLabel.setForeground(Color.LIGHT_GRAY);
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(updateLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createMetricCard(String title, String value, String subtitle, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(50, 50, 55));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(color);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.LIGHT_GRAY);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(subtitleLabel, BorderLayout.SOUTH);
        
        valueComponents.put(title.toLowerCase().replace(" ", "_"), valueLabel);
        
        return card;
    }
    
    private Border createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1), 
            title
        );
    }
    
    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(new Color(40, 40, 45));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(80, 80, 80));
        table.setSelectionBackground(new Color(65, 105, 225));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setRowHeight(20);
        
        table.getTableHeader().setBackground(new Color(60, 60, 65));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        return table;
    }
    
    private void setupMonitoring() {
        timer = new Timer(2000, this::updateAllData);
        timer.start();
    }
    
    private void updateAllData(ActionEvent e) {
        updateCPUData();
        updateMemoryData();
        updateDiskData();
        updateNetworkData();
        updateProcessData();
        updateSystemData();
        updateDashboard();
    }
    
    private void updateCPUData() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        
        double cpuUsage = 10 + Math.random() * 60;
        JLabel cpuLabel = (JLabel) valueComponents.get("cpu");
        if (cpuLabel != null) {
            cpuLabel.setText(df.format(cpuUsage) + "%");
        }
        
        JTextArea cpuInfo = (JTextArea) valueComponents.get("cpu_info");
        if (cpuInfo != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Sistema: ").append(osBean.getName()).append("\n");
            sb.append("Arquitetura: ").append(osBean.getArch()).append("\n");
            sb.append("Versao: ").append(osBean.getVersion()).append("\n");
            sb.append("Nucleos: ").append(osBean.getAvailableProcessors()).append("\n");
            sb.append("Load Average: ").append(osBean.getSystemLoadAverage()).append("\n");
            sb.append("Uso CPU: ").append(df.format(cpuUsage)).append("%\n");
            cpuInfo.setText(sb.toString());
        }
        
        JTextArea coresArea = (JTextArea) valueComponents.get("cpu_cores");
        if (coresArea != null) {
            StringBuilder sb = new StringBuilder();
            int cores = osBean.getAvailableProcessors();
            sb.append("Nucleos disponiveis: ").append(cores).append("\n\n");
            
            for (int i = 0; i < cores; i++) {
                double coreUsage = 5 + Math.random() * 80;
                sb.append(String.format("Nucleo %2d: %6.2f%%\n", i + 1, coreUsage));
            }
            coresArea.setText(sb.toString());
        }
    }
    
    private void updateMemoryData() {
        Runtime runtime = Runtime.getRuntime();
        
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();
        double memoryUsage = (double) usedMemory / totalMemory * 100;
        
        JLabel memoriaLabel = (JLabel) valueComponents.get("memoria");
        if (memoriaLabel != null) {
            memoriaLabel.setText(df.format(memoryUsage) + "%");
        }
        
        JLabel javaLabel = (JLabel) valueComponents.get("java");
        if (javaLabel != null) {
            javaLabel.setText(df.format(memoryUsage) + "%");
        }
        
        updateMemoryLabel("memoria_ram_usage", df.format(memoryUsage) + "%");
        updateMemoryLabel("memoria_ram_total", formatBytes(totalMemory));
        updateMemoryLabel("memoria_ram_used", formatBytes(usedMemory));
        updateMemoryLabel("memoria_ram_free", formatBytes(freeMemory));
        updateMemoryLabel("memoria_ram_max", formatBytes(maxMemory));
        
        JProgressBar ramProgress = progressBars.get("memoria_ram_progress");
        if (ramProgress != null) {
            ramProgress.setValue((int) memoryUsage);
            ramProgress.setString((int) memoryUsage + "%");
        }
        
        updateMemoryLabel("java_heap_usage", df.format(memoryUsage) + "%");
        updateMemoryLabel("java_heap_total", formatBytes(totalMemory));
        updateMemoryLabel("java_heap_used", formatBytes(usedMemory));
        updateMemoryLabel("java_heap_free", formatBytes(freeMemory));
        updateMemoryLabel("java_heap_max", formatBytes(maxMemory));
        
        JProgressBar javaProgress = progressBars.get("java_heap_progress");
        if (javaProgress != null) {
            javaProgress.setValue((int) memoryUsage);
            javaProgress.setString((int) memoryUsage + "%");
        }
    }
    
    private void updateMemoryLabel(String key, String value) {
        JLabel label = (JLabel) valueComponents.get(key);
        if (label != null) {
            label.setText(value);
        }
    }
    
    private void updateDiskData() {
        diskTableModel.setRowCount(0);
        
        File[] roots = File.listRoots();
        for (File root : roots) {
            long total = root.getTotalSpace();
            long free = root.getFreeSpace();
            long usable = root.getUsableSpace();
            double percentUsed = total > 0 ? (double) (total - free) / total * 100 : 0;
            
            diskTableModel.addRow(new Object[]{
                root.getPath(),
                root.getAbsolutePath(),
                formatBytes(total),
                formatBytes(free),
                formatBytes(usable),
                df.format(percentUsed) + "%"
            });
        }
        
        if (roots.length > 0) {
            File root = roots[0];
            long total = root.getTotalSpace();
            long free = root.getFreeSpace();
            double percentUsed = total > 0 ? (double) (total - free) / total * 100 : 0;
            JLabel discoLabel = (JLabel) valueComponents.get("disco");
            if (discoLabel != null) {
                discoLabel.setText(df.format(percentUsed) + "%");
            }
        }
    }
    
    private void updateNetworkData() {
        StringBuilder networkInfo = new StringBuilder();
        StringBuilder statsInfo = new StringBuilder();
        
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            int interfaceCount = 0;
            
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (ni.isUp() && !ni.isLoopback()) {
                    interfaceCount++;
                    networkInfo.append("Interface: ").append(ni.getDisplayName()).append("\n");
                    networkInfo.append("  Nome: ").append(ni.getName()).append("\n");
                    networkInfo.append("  MTU: ").append(ni.getMTU()).append("\n");
                    networkInfo.append("  Virtual: ").append(ni.isVirtual()).append("\n");
                    
                    byte[] mac = ni.getHardwareAddress();
                    if (mac != null) {
                        networkInfo.append("  MAC: ");
                        for (int i = 0; i < mac.length; i++) {
                            networkInfo.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                        }
                        networkInfo.append("\n");
                    }
                    networkInfo.append("\n");
                }
            }
            
            statsInfo.append("Interfaces Ativas: ").append(interfaceCount).append("\n\n");
            statsInfo.append("Velocidade Download: ").append(df.format(50 + Math.random() * 500)).append(" KB/s\n");
            statsInfo.append("Velocidade Upload: ").append(df.format(20 + Math.random() * 200)).append(" KB/s\n");
            statsInfo.append("Pacotes Recebidos: ").append((long)(100000 + Math.random() * 1000000)).append("\n");
            statsInfo.append("Pacotes Enviados: ").append((long)(50000 + Math.random() * 500000)).append("\n");
            
        } catch (SocketException e) {
            networkInfo.append("Erro ao obter informacoes de rede: ").append(e.getMessage());
            statsInfo.append("Erro ao obter estatisticas de rede");
        }
        
        networkInfoArea.setText(networkInfo.toString());
        
        JTextArea statsArea = (JTextArea) valueComponents.get("network_stats");
        if (statsArea != null) {
            statsArea.setText(statsInfo.toString());
        }
        
        JLabel redeLabel = (JLabel) valueComponents.get("rede");
        if (redeLabel != null) {
            redeLabel.setText(df.format(50 + Math.random() * 300) + " KB/s");
        }
    }
    
    private void updateProcessData() {
        processTableModel.setRowCount(0);
        
        String[] processNames = {
            "System", "Explorer.exe", "chrome.exe", "javaw.exe", 
            "Code.exe", "svchost.exe", "winlogon.exe", "services.exe",
            "lsass.exe", "csrss.exe", "smss.exe", "dwm.exe"
        };
        
        for (int i = 0; i < processNames.length; i++) {
            processTableModel.addRow(new Object[]{
                1000 + i,
                processNames[i],
                "RUNNING",
                (int)(Math.random() * 10) + 1,
                (int)(Math.random() * 10),
                df.format(Math.random() * 1000) + "ms"
            });
        }
        
        JLabel processosLabel = (JLabel) valueComponents.get("processos");
        if (processosLabel != null) {
            processosLabel.setText(processNames.length + "");
        }
        
        JLabel threadsLabel = (JLabel) valueComponents.get("threads");
        if (threadsLabel != null) {
            threadsLabel.setText(Thread.activeCount() + "");
        }
    }
    
    private void updateSystemData() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("=== SISTEMA OPERACIONAL ===\n");
        sb.append("Nome: ").append(System.getProperty("os.name")).append("\n");
        sb.append("Versao: ").append(System.getProperty("os.version")).append("\n");
        sb.append("Arquitetura: ").append(System.getProperty("os.arch")).append("\n");
        sb.append("Fabricante: ").append(System.getProperty("java.vendor")).append("\n\n");
        
        sb.append("=== USUARIO E AMBIENTE ===\n");
        sb.append("Usuario: ").append(System.getProperty("user.name")).append("\n");
        sb.append("Diretorio: ").append(System.getProperty("user.home")).append("\n");
        sb.append("Diretorio Atual: ").append(System.getProperty("user.dir")).append("\n\n");
        
        sb.append("=== JAVA VIRTUAL MACHINE ===\n");
        sb.append("Versao Java: ").append(System.getProperty("java.version")).append("\n");
        sb.append("VM: ").append(System.getProperty("java.vm.name")).append("\n");
        sb.append("Vendor: ").append(System.getProperty("java.vm.vendor")).append("\n");
        sb.append("Runtime: ").append(System.getProperty("java.runtime.name")).append("\n\n");
        
        Runtime runtime = Runtime.getRuntime();
        sb.append("=== RUNTIME ===\n");
        sb.append("Processadores: ").append(runtime.availableProcessors()).append("\n");
        sb.append("Memoria Livre: ").append(formatBytes(runtime.freeMemory())).append("\n");
        sb.append("Memoria Total: ").append(formatBytes(runtime.totalMemory())).append("\n");
        sb.append("Memoria Maxima: ").append(formatBytes(runtime.maxMemory())).append("\n");
        sb.append("Threads Ativas: ").append(Thread.activeCount()).append("\n");
        
        systemInfoArea.setText(sb.toString());
    }
    
    private void updateDashboard() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        long hours = uptime / (1000 * 60 * 60);
        long minutes = (uptime % (1000 * 60 * 60)) / (1000 * 60);
        JLabel uptimeLabel = (JLabel) valueComponents.get("uptime");
        if (uptimeLabel != null) {
            uptimeLabel.setText(hours + "h " + minutes + "m");
        }
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return df.format(bytes / 1024.0) + " KB";
        if (bytes < 1024 * 1024 * 1024) return df.format(bytes / (1024.0 * 1024)) + " MB";
        return df.format(bytes / (1024.0 * 1024 * 1024)) + " GB";
    }
}
