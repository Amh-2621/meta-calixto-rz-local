LIC_FILES_CHKSUM = "file://docs/license.rst;md5=b2c740efedc159745b9b31f88ff03dde"

require fiptool-native.inc

URL = "git://github.com/Amh-2621/calixto-rz-atf-local.git"
BRANCH = "v2.9/rz"
SRCREV = "23cdf2d7ccd4fcf99bee55551122797c6ae009be"

SRC_URI = "${URL};protocol=https;branch=${BRANCH}"

PV = "2.9+git${SRCPV}"
PR = "r1"



