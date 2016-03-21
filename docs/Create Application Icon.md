# Create application icon
The default icon for an executable JAR is a coffee cup. To add a custom icon, we need to create an .icns file.

Use the following commands to download a sample PNG image, resize the image to appropriate dimensions for an icon, and convert it into the .icns format:

```
mkdir openhab-fx.iconset
sips -z 128 128 openhab-fx.png --out openhab-fx.iconset/icon_128x128.png
iconutil --convert icns openhab-fx.iconset
```

*Note: Ignore the Missing image for variant warnings.*