require recipes-bsp/u-boot/u-boot-renesas.inc

COMPATIBLE_MACHINE_rzg2l = "(rzg2l-optima-1gb|rzg2l-optima-2gb|rzg2l-versa-1gb|rzg2l-versa-2gb|rzg2ul-tiny-1gb|rzg2ul-tiny-256mb|rzg2ul-tiny-512mb)"

UBOOT_URI = "git://github.com/Amh-2621/calixto-rz-uboot-local.git;protocol=https"
UBOOT_BRANCH = "v2021.10"
UBOOT_REV ?= "a6c2c70fbd5cfe17f2f769dcdeaf9e27d90004b7"

PV = "2021.10+git${SRCPV}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"

do_deploy:append() {
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    install -m 644 ${B}/${config}/${UBOOT_SREC} ${DEPLOYDIR}/u-boot-elf-${type}-${PV}-${PR}.${UBOOT_SREC_SUFFIX}
                    cd ${DEPLOYDIR}
                    ln -sf u-boot-elf-${type}-${PV}-${PR}.${UBOOT_SREC_SUFFIX} u-boot-elf-${type}.${UBOOT_SREC_SUFFIX}
                fi
            done
            unset j
        done
        unset i
    else
        install -m 644 ${B}/${UBOOT_SREC} ${DEPLOYDIR}/${UBOOT_SREC_IMAGE}
        cd ${DEPLOYDIR}
        rm -f ${UBOOT_SREC} ${UBOOT_SREC_SYMLINK}
        ln -sf ${UBOOT_SREC_IMAGE} ${UBOOT_SREC_SYMLINK}
        ln -sf ${UBOOT_SREC_IMAGE} ${UBOOT_SREC}
    fi
}
