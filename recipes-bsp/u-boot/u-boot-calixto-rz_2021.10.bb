require u-boot-common_${PV}.inc
require u-boot.inc

DEPENDS += "bc-native dtc-native"

UBOOT_URL = "git://github.com/Amh-2621/calixto-rz-uboot-local.git"

BRANCH = "v2021.10"

SRC_URI = "${UBOOT_URL};branch=${BRANCH}"
SRCREV = "a6c2c70fbd5cfe17f2f769dcdeaf9e27d90004b7"
PV = "v2021.10+git${SRCPV}"
