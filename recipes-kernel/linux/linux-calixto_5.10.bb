DESCRIPTION = "Linux kernel for the RZG2 based board"

require recipes-kernel/linux/linux-yocto.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"
COMPATIBLE_MACHINE_rzg2l = "(rzg2l-optima-1gb|rzg2l-versa-1gb|rzg2l-optima-2gb|rzg2l-versa-2gb|rzv2l-optima-1gb|rzv2l-optima-2gb|rzv2l-versa-1gb|rzv2l-versa-2gb|rzg2ul-tiny-512mb|rzg2ul-tiny-256mb|rzg2ul-tiny-1gb)"

KERNEL_URL = " \
    git://github.com/Amh-2621/linux-kernel-local-rz.git"

BRANCH = "5.10.131"
SRCREV = "a46bd22c4a3a7f240960861cab091e73889f188c"

SRC_URI = "${KERNEL_URL};protocol=https;nocheckout=1;branch=${BRANCH}"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.10.131"

PV = "${LINUX_VERSION}+git${SRCPV}"
PR = "r1"
KERNEL_VERSION_SANITY_SKIP="1"


KBUILD_DEFCONFIG = "rzg2l_calixto_default_defconfig"
KCONFIG_MODE = "alldefconfig"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"


do_configure_append() {
    # Define the path to the DTS file in the kernel source directory
    kernel_dts_dir="${S}/arch/arm64/boot/dts/renesas"

    # Initialize variables
    original_dts=""
    new_dts=""

    # Set source and target DTS filenames based on MACHINE
    if [ "${MACHINE}" = "rzg2l-optima-1gb" ]; then
        original_dts="${kernel_dts_dir}/rzg2l-calixto-optima_1GB.dts"
        new_dts="${kernel_dts_dir}/rzg2l-calixto-optima.dts"
    elif [ "${MACHINE}" = "rzg2l-optima-2gb" ]; then
        original_dts="${kernel_dts_dir}/rzg2l-calixto-optima_2GB.dts"
        new_dts="${kernel_dts_dir}/rzg2l-calixto-optima.dts"
    elif [ "${MACHINE}" = "rzg2l-versa-1gb" ]; then
        original_dts="${kernel_dts_dir}/rzg2l-calixto-versa_1GB.dts"
        new_dts="${kernel_dts_dir}/rzg2l-calixto-versa.dts"
    elif [ "${MACHINE}" = "rzg2l-versa-2gb" ]; then
        original_dts="${kernel_dts_dir}/rzg2l-calixto-versa_2GB.dts"
        new_dts="${kernel_dts_dir}/rzg2l-calixto-versa.dts"
    elif [ "${MACHINE}" = "rzv2l-optima-1gb" ]; then
        original_dts="${kernel_dts_dir}/rzv2l-calixto-optima_1GB.dts"
        new_dts="${kernel_dts_dir}/rzv2l-calixto-optima.dts"
    elif [ "${MACHINE}" = "rzv2l-optima-2gb" ]; then
        original_dts="${kernel_dts_dir}/rzv2l-calixto-optima_2GB.dts"
        new_dts="${kernel_dts_dir}/rzv2l-calixto-optima.dts"
    elif [ "${MACHINE}" = "rzv2l-versa-1gb" ]; then
        original_dts="${kernel_dts_dir}/rzv2l-calixto-versa_1GB.dts"
        new_dts="${kernel_dts_dir}/rzv2l-calixto-versa.dts"
    elif [ "${MACHINE}" = "rzv2l-versa-2gb" ]; then
        original_dts="${kernel_dts_dir}/rzv2l-calixto-versa_2GB.dts"
        new_dts="${kernel_dts_dir}/rzv2l-calixto-versa.dts"
    elif [ "${MACHINE}" = "rzg2ul-tiny-512mb" ]; then
        original_dts="${kernel_dts_dir}/rzg2ul-calixto-tiny_512MB.dts"
        new_dts="${kernel_dts_dir}/rzg2ul-calixto-tiny.dts"
    elif [ "${MACHINE}" = "rzg2ul-tiny-256mb" ]; then
        original_dts="${kernel_dts_dir}/rzg2ul-calixto-tiny_256MB.dts"
        new_dts="${kernel_dts_dir}/rzg2ul-calixto-tiny.dts"
    elif [ "${MACHINE}" = "rzg2ul-tiny-1gb" ]; then
        original_dts="${kernel_dts_dir}/rzg2ul-calixto-tiny_1GB.dts"
        new_dts="${kernel_dts_dir}/rzg2ul-calixto-tiny.dts" 
  fi

    # Copy the DTS file if it was configured and exists
    if [ -n "$original_dts" ] && [ -f "$original_dts" ]; then
        cp "$original_dts" "$new_dts"
        echo "Copied DTS file from $original_dts to $new_dts"
    elif [ -n "$original_dts" ]; then
        echo "Warning: DTS file $original_dts not found"
    fi
}



do_deploy_append() {
	for dtbf in ${KERNEL_DEVICETREE}; do
		dtb=`normalize_dtb "$dtbf"`
		dtb_ext=${dtb##*.}
		dtb_base_name=`basename $dtb .$dtb_ext`
		for type in ${KERNEL_IMAGETYPE_FOR_MAKE}; do
			ln -sf $dtb_base_name-${KERNEL_DTB_NAME}.$dtb_ext $deployDir/$type-$dtb_base_name.$dtb_ext
		done
	done
}

#addtask after do_patch before do_kernel_configme

# Fix race condition, which can causes configs in defconfig file be ignored
do_kernel_configme[depends] += "virtual/${TARGET_PREFIX}binutils:do_populate_sysroot"
do_kernel_configme[depends] += "virtual/${TARGET_PREFIX}gcc:do_populate_sysroot"
do_kernel_configme[depends] += "bc-native:do_populate_sysroot bison-native:do_populate_sysroot"

# Fix error: openssl/bio.h: No such file or directory
DEPENDS += "openssl-native"
