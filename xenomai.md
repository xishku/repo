1	资源下载
1.1	系统内核下载
https://mirrors.tuna.tsinghua.edu.cn/kernel/v5.x/
linux-5.4.105.tar.gz

1.2	ipipe软件补丁
https://xenomai.org/downloads/ipipe/v5.x/x86/
ipipe-core-5.4.105-x86-4.patch

1.3	xenomai3.1.x版本
https://source.denx.de/Xenomai/xenomai/-/tree/stable/v3.1.x
xenomai-stable-v3.1.x

2	编译linux-xenomai并安装
2.1	打补丁ipipe
1, 压缩包解压
sudo tar -zvxf linux-5.4.105.tar.gz 
sudo tar -zvxf xenomai-stable-v3.1.x.tar.gz 

2,内核打补丁 (进入到linux内核目录下运行preprare-kernel.sh不会报错，进入到xenomaid scripts目录下运行prepare-kernel.sh会报错)
#> cd linux-5.4.105/
#> cp -r ../ipipe-core-5.4.105-x86-4.patch ./
#> ../xenomai-stable-v3.1.x/scripts/prepare-kernel.sh --arch=x86_64 --ipipe=ipipe-core-5.4.105-x86-4.patch

2.2	配置menuconfig
2.2.1	参考配置1
https://blog.csdn.net/guimotion/article/details/115483150
sudo make menuconfig

5.1进入Processor type and features
5.1.1 Linux guest support(switch off)[*] to [] 禁用关闭
5.1.2 Processor family 选择core2/new xeon
5.1.3Multi-core scheduler support多核策略支持(switch off)[*] to [] 禁用关闭

5.2进入Power management and ACPI options
5.2.1suspend to RAM and standby(switch off)[*] to [] 禁用关闭
5.2.2Hibernation(aka 'suspend to disk')(switch off)[*] to [] 禁用关闭
5.2.3CPU Frequency scaling--->CPU Frequency scaling(switch off)[*] to [] 禁用关闭
5.2.4ACPI(Advanced Configuration and Power Interface) Support(switch off)[*] to [] 禁用关闭
5.2.5CPU Idle--->CPU idle PM support(switch off)[*] to [] 禁用关闭

5.3进入Memory Mangement options
5.3.1Transparment Hugepage Support(switch off)[*] to [] 禁用关闭
5.3.2Contiguous Memory Allocator(switch off)[*] to [] 禁用关闭
5.3.3Allow for memory compaction(switch off)[*] to [] 禁用关闭
5.3.4 Page migration(switch off)[*] to [] 禁用关闭

5.4进入General setup
5.4.1Timers subsystem--->High Resolution Timer Support(switch on)[*] 

5.5进入xenomai/cobalt
5.5.1进入Sizes and static limits --->
(4096)Number of registry slots
(4096)Size of system heap(kb)
(256)Size of private heap(kb)
(256)Size of shared heap(kb)
(512)Maxinum number of POSIX timers per process

5.5.2进入Drivers--->RTnet--->RTnet, TCP/IP socket interface(switch on)[] to [M] 启用驱动
5.5.2.1New Intel(R) PRO/1000(Gigabit)(new) (switch on)[] to [M] 启用驱动
5.5.2.2Realtek 8169(Gigabit)(new) (switch on)[] to [M] 启用驱动
5.5.2.2Loopback (switch on)[] to [M] 启用驱动

5.3进入Drivers--->RTnet--->Add-ons--->Real-Time Capturing Support(new)(switch on)[] to [M] 启用驱动

==============
https://stackoverflow.com/questions/40344484/cant-load-self-compiled-linux-kernel
Power management and ACPI options --->
    [*] ACPI (Advanced Configuration and Power Interface) Support --->
Processor type and features  --->
    [*] EFI runtime service support 
    [*]   EFI stub support
Firmware Drivers  --->
   EFI (Extensible Firmware Interface) Support  --->
       <*> EFI Variable Support via sysfs
       
==============

2.2.2	参考配置2
下面是另一篇博文的设置：
https://blog.csdn.net/weixin_48395629/article/details/115443166?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-2.no_search_link&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-2.no_search_link


#进入menuconfig界面，会看影响xenomai实时性的警告信息：
#*** WARNING! Page migration (CONFIG_MIGRATION) may increase ***
#*** latency. ***
#*** WARNING! At least one of APM, CPU frequency scaling, ACPI ‘processor‘ ***
#*** or CPU idle features is enabled. Any of these options may ***
#*** cause troubles with Xenomai. You should disable them. ***

#具体配置如下所示：   
    * General setup
      --> Local version - append to kernel release: -xenomai-3.1
      --> Timers subsystem
          --> High Resolution Timer Support (Enable)
    * Xenomai/cobalt
      --> Sizes and static limits
        --> Number of registry slots (512 --> 4096)
        --> Size of system heap (Kb) (512 --> 4096)
        --> Size of private heap (Kb) (64 --> 256)
        --> Size of shared heap (Kb) (64 --> 256)
        --> Maximum number of POSIX timers per process (128 --> 512)
      --> Drivers
        --> RTnet
            --> RTnet, TCP/IP socket interface (Enable)
                --> Drivers
                    --> New intel(R) PRO/1000 PCIe (Enable)
                    --> Realtek 8169 (Enable)
                    --> Loopback (Enable)
            --> Add-Ons
                --> Real-Time Capturing Support (Enable)
    * Power management and ACPI options
      --> CPU Frequency scaling
          --> CPU Frequency scaling (Disable)
      --> ACPI (Advanced Configuration and Power Interface) Support
          --> Processor (Disable)
      --> CPU Idle
          --> CPU idle PM support (Disable)
    * Pocessor type and features
      --> Enable maximum number of SMP processors and NUMA nodes (Disable)
      // Ref : http://xenomai.org/pipermail/xenomai/2017-September/037718.html
      --> Processor family
          --> Core 2/newer Xeon (if "cat /proc/cpuinfo | grep family" returns 6, set as Generic otherwise)
  
// Xenomai will issue a warning about CONFIG_MIGRATION, disable those in this orde
*Memory Mangement options
      --> Transparent Hugepage Support (Disable)
      --> Allow for memory compaction (Disable)
      --> Contiguous Memory Allocation (Disable)
      --> Allow for memory compaction
          --> Page Migration (Disable)
    * Device Drivers
      --> Staging drivers
          --> Unisys SPAR driver support
             --> Unisys visorbus driver (Disable)


2.2.3	参考配置3
https://www.codenong.com/cs106668796/


Sudo make mrproper #清空历史配置，从头开始配置
Sudo make menuconfig # 可以在首页看到下面的warning
 

下面的配置以消除warning为主进行配置：
消除Page migration的配置，必须按顺序配置，因为这些配置之间有依赖，只有前面的配置了，后一步才可以继续配置。
5.3进入Memory Mangement options
5.3.1Transparment Hugepage Support(switch off)[*] to [] 禁用关闭
5.3.2Contiguous Memory Allocator(switch off)[*] to [] 禁用关闭
5.3.3Allow for memory compaction(switch off)[*] to [] 禁用关闭
5.3.4 Page migration(switch off)[*] to [] 禁用关闭
 


消除APM，CPU，ACPI， CPU idle的配置, 必须按顺序配置，因为这些配置之间有依赖，只有前面的配置了，后一步才可以继续配置。

Processor type and feature(enter)
Linux gust support(switch off)[*] to[ ]
Processor family(enter)-We have chosen core 2/new Xeon
Then, we would like to close multi-core scheduler support.(N)
Power management and ACPI options —> (enter)
suspend to RAM and standby----switch off
Hibernation (aka ‘suspend to disk’)----switch off
CPU Frequency scaling —> CPU Frequency scaling----switch off

ACPI (Advanced Configuration and Power Interface) Support ---->Processor ----switch off
CPU Idle —> CPU idle PM support —switch off
 


配置xenomai/cobalt，打开RTnet
   * Xenomai/cobalt
      --> Sizes and static limits
        --> Number of registry slots (512 --> 4096)
        --> Size of system heap (Kb) (512 --> 4096)
        --> Size of private heap (Kb) (64 --> 256)
        --> Size of shared heap (Kb) (64 --> 256)
        --> Maximum number of POSIX timers per process (128 --> 512)
      --> Drivers
        --> RTnet
            --> RTnet, TCP/IP socket interface (Enable)
                --> Drivers
                    --> New intel(R) PRO/1000 PCIe (Enable)
                    --> Realtek 8169 (Enable)
                    --> Loopback (Enable)
            --> Add-Ons
                --> Real-Time Capturing Support (Enable)

配置SMI，撤销下面的SMI
  

禁止下面的driver
 

2.2.4	参考配置4
在配置3的基础上，允许：Linux 5.4.105.xenomai-3.1.x-acpi
Power management and ACPI options —> (enter)
ACPI (Advanced Configuration and Power Interface) Support ---->Processor ----switch off

 
2.3	编译xenomai内核
sudo apt install kernel-package
修改.config文件
CONFIG_SYSTEM_TRUSTED_KEYS=""
开始编译
sudo CONCURRENCY_LEVEL=$(nproc) make-kpkg --rootcmd fakeroot --initrd kernel_image kernel_headers


2.4	安装xenomai内核
8.1更换名称
cp -r linux-headers-5.4.105_5.4.105-10.00.Custom_amd64.deb linux-headers-5.4.105_xenomai3.1.deb
cp linux-image-5.4.105_5.4.105-10.00.Custom_amd64.deb linux-image-5.4.105_xenomai3.1.deb
8.2修改权限
chmod 755 linux-image-5.4.105_xenomai3.1.deb
chmod 755 linux-headers-5.4.105_xenomai3.1.deb
8.3安装内核数据包
sudo dpkg -i linux-headers-5.4.105_xenomai3.1.deb linux-image-5.4.105_xenomai3.1.deb

9、追加xenomai root用户权限
sudo addgroup xenomai --gid 1234
sudo addgroup root xenomai
sudo usermod -a -G xenomai $USER
——————————————

10、配置开机启动内核
sudo gedit /etc/default/grub
10.1修改内容如下：
#GRUB_DEFAULT=0
GRUB_DEFAULT="Advanced options for Ubuntu>Ubuntu, with Linux 5.4.105-xenomai-3.1.x"

#GRUB_TIMEOUT_STYLE=hidden
#GRUB_TIMEOUT=0
GRUB_TIMEOUT=3

GRUB_DISTRIBUTOR=`lsb_release -i -s 2> /dev/null || echo Debian`

#GRUB_CMDLINE_LINUX_DEFAULT="quiet splash"
GRUB_CMDLINE_LINUX_DEFAULT="quiet splash xenomai.allowed_group=1234"
GRUB_CMDLINE_LINUX=""
-------------------------------------------------------
sudo update-grub
sudo reboot

参考配置1+2，最后的截图：
 

参考配置3，最后的截图。
 

参考配置4，最后的截图。
 
若上图中没有rtnet，可通过命令modprobe rtnet加载
3	安装xenomai用户空间程序
前置条件：先安装FUSE文件系统
sudo apt install libfuse-dev
sudo apt install fuse-emulator-gtk
sudo apt install pkg-config
sudo apt install findutils
sudo apt install debhelper
sudo apt install libtool
sudo apt install libltdl-dev


cd xenomai-stable-v3.1.x/
sudo ./scripts/bootstrap  (注意，必须要在这个目录下面运行./scripts/， 直接运行./bootstrap会报错)
sudo ./configure --with-pic --with-core=cobalt --enable-smp --disable-tls --enable-dlopen-libs --disable-clock-monotonic-raw
make -j4
sudo make install


设置环境变量：更新bashrc
echo '
### Xenomai
export XENOMAI_ROOT_DIR=/usr/xenomai
export XENOMAI_PATH=/usr/xenomai
export PATH=$PATH:$XENOMAI_PATH/bin:$XENOMAI_PATH/sbin
export PKG_CONFIG_PATH=$PKG_CONFIG_PATH:$XENOMAI_PATH/lib/pkgconfig
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$XENOMAI_PATH/lib
export OROCOS_TARGET=xenomai
' >> ~/.xenomai_rc

echo 'source ~/.xenomai_rc' >> ~/.bashrc
source ~/.bashrc


11.5追加权限
sudo chmod -R 777 /dev/rtdm/memdev-private
sudo chmod -R 777 /dev/rtdm/memdev-shared
