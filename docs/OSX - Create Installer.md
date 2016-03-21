# Build Mac OS X application with installer

The following commands move the icon file into the package folder and then use the javapackager tool to build the ShowTime.app appplication and wrap it in into an installer:

```
mkdir -p package/macosx
cp openhab-fx.icns package/macosx
jdk=$(/usr/libexec/java_home)
$jdk/bin/javapackager -version
$jdk/bin/javapackager -deploy -native dmg \
   -srcfiles openhab-fx.jar -appclass openhab-fx -name openhab-fx \
   -outdir deploy -outfile openhab-fx -v
cp deploy/bundles/openhab-fx-1.0.dmg openhab-fx-installer.dmg
```

The executable JAR file ShowTime.jar checks in at a mere 0.001 MB, but the installer file show-time-installer.dmg is a whopping 66 MB.  The reason is that it bundles the JRE (an Apple requirement for publishing Java programs to the Mac OS X AppStore).

When you are ready to distribute to the public, you'll want to sign your application with a Developer ID certificate.  Use the productsign tool to apply a certificate.