require conf/machine/include/soc-family.inc
SOC_FAMILY = "rzg2l-family:mali-family"

# Machine configuration
MACHINE_FEATURES = "opengles opencl"

IMAGE_FSTYPES:append = " tar.bz2 ext4"

# Configuration for ARM Trusted Firmware
EXTRA_IMAGEDEPENDS += " trusted-firmware-a-calixto-rz"

# flash writer
EXTRA_IMAGEDEPENDS += " flash-writer-calixto-rz"

# Firmware-pack
EXTRA_IMAGEDEPENDS += " firmware-pack-calixto-rz"

# U-boot
EXTRA_IMAGEDEPENDS += " u-boot-calixto-rz"
PREFERRED_PROVIDER_virtual/bootloader = "u-boot-calixto-rz"

# Linux kernel configuration
PREFERRED_PROVIDER_virtual/kernel ?= "linux-calixto"
PREFERRED_VERSION_linux-calixto ?= "6.1%"

