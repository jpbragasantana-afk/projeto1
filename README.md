# projeto1 = Monitoramento CPU

import psutil
import GPUtil

def monitoramento_completo():
    print("=== MONITORAMENTO DETALHADO ===\n")
    
    # CPU Detalhada
    print("🖥  CPU:")
    print(f"   Uso total: {psutil.cpu_percent(interval=1)}%")
    print(f"   Uso por núcleo: {[f'{x}%' for x in psutil.cpu_percent(percpu=True)]}")
    print(f"   Frequência: {psutil.cpu_freq().current:.0f} MHz")
    print(f"   Núcleos: {psutil.cpu_count()} físicos, {psutil.cpu_count(logical=True)} lógicos")
    
    # RAM Detalhada
    print("\n💾 MEMÓRIA RAM:")
    mem = psutil.virtual_memory()
    print(f"   Uso: {mem.percent}%")
    print(f"   Total: {mem.total / (1024**3):.1f} GB")
    print(f"   Disponível: {mem.available / (1024**3):.1f} GB")
    
    # Discos
    print("\n💽 DISCOS:")
    for part in psutil.disk_partitions():
        try:
            uso = psutil.disk_usage(part.mountpoint)
            print(f"   {part.device}: {uso.percent}% usado")
        except:
            pass
    
    # Rede
    print("\n🌐 REDE:")
    net = psutil.net_io_counters()
    print(f"   Enviados: {net.bytes_sent / (1024**2):.1f} MB")
    print(f"   Recebidos: {net.bytes_recv / (1024**2):.1f} MB")

monitoramento_completo()
