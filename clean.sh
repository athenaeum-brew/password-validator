rm -rf target/checkout
rm -rf target/prepare
rm -rf pom.xml.releaseBackup
git checkout pom.xml
git tag -d password-validator-1.0.0
git push origin --delete refs/tags/password-validator-1.0.0
