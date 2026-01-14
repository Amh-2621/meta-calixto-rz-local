DESCRIPTION = "Linux kernel from the Renesas RZ BSP based on linux-6.1.y-cip"

require recipes-kernel/linux/linux-yocto.inc
require linux-common_6.1.inc

LINUX_VERSION ?= "6.1.107-cip28"
KBUILD_DEFCONFIG ?= "rzg2l_calixto_default_defconfig"
KCONFIG_MODE ?= "alldefconfig"

KERNEL_URL ?= "git://github.com/Amh-2621/linux-kernel-local-rz.git"

KERNEL_BRANCH ?= "6.1"
KERNEL_REV ?= "f159a2c061930cdd676f1bec838ef44be94b053c"

SRC_URI:append = "${@bb.utils.contains('DISTRO_FEATURES','docker', ' file://docker.cfg', '', d)}"

do_configure:append() {
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

