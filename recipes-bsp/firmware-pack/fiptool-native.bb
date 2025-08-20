LIC_FILES_CHKSUM = "file://docs/license.rst;md5=b2c740efedc159745b9b31f88ff03dde"

require fiptool-native.inc

URL = "git://github.com/Amh-2621/calixto-rz-atf-local.git"
BRANCH = "v2.7/rz"
SRCREV = "a9123a3357e9e2fcc5a90a362fc1d23a42d4497d"

SRC_URI = "${URL};protocol=https;branch=${BRANCH}"

PV = "2.7+git${SRCPV}"
PR = "r1"



