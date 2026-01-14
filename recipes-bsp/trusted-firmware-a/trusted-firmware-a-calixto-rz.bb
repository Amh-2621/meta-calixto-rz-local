DESCRIPTION = "Trusted Firmware-A for CALIXTO RZ based SOM's"

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = " \
	file://${WORKDIR}/git/docs/license.rst;md5=b2c740efedc159745b9b31f88ff03dde \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

S = "${WORKDIR}/git"

BRANCH = "v2.9/rz"

SRC_URI = " \
	git://github.com/Amh-2621/calixto-rz-atf-local.git;branch=${BRANCH};protocol=https \
"

SRCREV = "dea403f0679497a2ac2d306e095b4fc6aeb8c916"

PV = "v2.9+git"

COMPATIBLE_MACHINE = "(rzg2l-optima-1gb|rzg2l-optima-2gb|rzg2l-versa-1gb|rzg2l-versa-2gb|rzg2ul-tiny-512mb|rzg2ul-tiny-256mb|rzg2ul-tiny-1gb)"

#PLATFORM ?= "rzg"

# requires CROSS_COMPILE set by hand as there is no configure script
export CROSS_COMPILE="${TARGET_PREFIX}"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"


PLATFORM_rzg2l-optima-1gb = "g2l"
EXTRA_FLAGS_rzg2l-optima-1gb = "BOARD=CALIXTO_RZG2L_OPTIMA_1GB"
PLATFORM_rzg2l-optima-2gb = "g2l"
EXTRA_FLAGS_rzg2l-optima-2gb = "BOARD=CALIXTO_RZG2L_OPTIMA_2GB"
PLATFORM_rzg2l-versa-1gb = "g2l"
EXTRA_FLAGS_rzg2l-versa-1gb = "BOARD=CALIXTO_RZG2L_VERSA_1GB"
PLATFORM_rzg2l-versa-2gb = "g2l"
EXTRA_FLAGS_rzg2l-versa-2gb = "BOARD=CALIXTO_RZG2L_VERSA_2GB"
PLATFORM_rzv2l-optima-1gb = "v2l"
EXTRA_FLAGS_rzv2l-optima-1gb = "BOARD=CALIXTO_RZV2L_OPTIMA_1GB"
PLATFORM_rzv2l-optima-2gb = "v2l"
EXTRA_FLAGS_rzv2l-optima-2gb = "BOARD=CALIXTO_RZV2L_OPTIMA_2GB"
PLATFORM_rzv2l-versa-1gb = "v2l"
EXTRA_FLAGS_rzv2l-versa-1gb = "BOARD=CALIXTO_RZV2L_VERSA_1GB"
PLATFORM_rzv2l-versa-2gb = "v2l"
EXTRA_FLAGS_rzv2l-versa-2gb = "BOARD=CALIXTO_RZV2L_VERSA_2GB"
PLATFORM_rzg2ul-tiny-256mb = "g2ul"
EXTRA_FLAGS_rzg2ul-tiny-256mb = "BOARD=CALIXTO_RZG2UL_TINY_256MB DEVICE_TYPE=2"
PLATFORM_rzg2ul-tiny-512mb = "g2ul"
EXTRA_FLAGS_rzg2ul-tiny-512mb = "BOARD=CALIXTO_RZG2UL_TINY_512MB DEVICE_TYPE=2"
PLATFORM_rzg2ul-tiny-1gb = "g2ul"
EXTRA_FLAGS_rzg2ul-tiny-1gb = "BOARD=CALIXTO_RZG2UL_TINY_1GB DEVICE_TYPE=2"



PMIC_BUILD_DIR = "${S}/build_pmic"

FILES_${PN} = "/boot "
SYSROOT_DIRS += "/boot"

# FILESEXTRAPATHS_append := "${THISDIR}/files"
#SRC_URI += " \
#	file://0001-plat-renesas-rz-Disable-unused-CRYPTO_SUPPORT.patch \
#"

ECC_FLAGS = " DDR_ECC_ENABLE=1 "
ECC_FLAGS += "${@oe.utils.conditional("ECC_MODE", "ERR_DETECT", "DDR_ECC_DETECT=1", "",d)}"
ECC_FLAGS += "${@oe.utils.conditional("ECC_MODE", "ERR_DETECT_CORRECT", "DDR_ECC_DETECT_CORRECT=1", "",d)}"
EXTRA_FLAGS:append = "${@oe.utils.conditional("USE_ECC", "1", " ${ECC_FLAGS} ", "",d)}"
PMIC_EXTRA_FLAGS:append = "${@oe.utils.conditional("USE_ECC", "1", " ${ECC_FLAGS} ", "",d)}"

do_compile() {
	oe_runmake PLAT=${TFA_PLATFORM} ${TFA_BOARD} bl2 bl31

	if [ "${PMIC_SUPPORT}" = "1" ]; then
		oe_runmake PLAT=${TFA_PLATFORM} ${PMIC_EXTRA_FLAGS} BUILD_PLAT=${PMIC_BUILD_DIR} bl2 bl31
	fi
}

do_install() {
	install -d ${D}/boot
	install -m 644 ${S}/build/${TFA_PLATFORM}/release/bl2.bin ${D}/boot/bl2-${MACHINE}.bin
	install -m 644 ${S}/build/${TFA_PLATFORM}/release/bl31.bin ${D}/boot/bl31-${MACHINE}.bin

	if [ "${PMIC_SUPPORT}" = "1" ]; then
		install -m 0644 ${PMIC_BUILD_DIR}/bl2.bin ${D}/boot/bl2-${MACHINE}_pmic.bin
		install -m 0644 ${PMIC_BUILD_DIR}/bl31.bin ${D}/boot/bl31-${MACHINE}_pmic.bin
	fi
}

do_deploy() {
    # Create deploy folder
    install -d ${DEPLOYDIR}

    # Copy IPL to deploy folder
    install -m 0644 ${S}/build/${TFA_PLATFORM}/release/bl2/bl2.elf ${DEPLOYDIR}/bl2-${MACHINE}.elf
    install -m 0644 ${S}/build/${TFA_PLATFORM}/release/bl2.bin ${DEPLOYDIR}/bl2-${MACHINE}.bin
    install -m 0644 ${S}/build/${TFA_PLATFORM}/release/bl31/bl31.elf ${DEPLOYDIR}/bl31-${MACHINE}.elf
    install -m 0644 ${S}/build/${TFA_PLATFORM}/release/bl31.bin ${DEPLOYDIR}/bl31-${MACHINE}.bin
    
    
    if [ "${PMIC_SUPPORT}" = "1" ]; then
		install -m 0644 ${PMIC_BUILD_DIR}/bl2.bin ${DEPLOYDIR}/bl2-${MACHINE}_pmic.bin
		install -m 0644 ${PMIC_BUILD_DIR}/bl31.bin ${DEPLOYDIR}/bl31-${MACHINE}_pmic.bin
    fi
}

addtask deploy before do_build after do_compile
