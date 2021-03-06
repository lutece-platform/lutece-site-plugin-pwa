![](http://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=plugin-pwa-deploy)
# Plugin PWA

![](http://dev.lutece.paris.fr/plugins/plugin-pwa/images/pwa.png)

## Introduction

This plugin let transform a Lutece site into a Progressive Web Application.

The site thus becomes installable on a mobile phone or on the user's desktop using the information from a **Manifest** file (icon, shortcut name, ...).

Resource loading strategies are optimized with a **Service** Worker who exploits a local cache.

## Configuration

Add in the file **page_frameset.html** the following code before the end of the `head` tag so to add the **Manifest** of the webapp
```

        <!- Progressive Webapp Manifest -->
        ${pwa_manifest_link}
    </head>

```


Manifest attributes are customizable in the **Site Properties Management** of the **Site** menu of the Lutece's Back Office.

Add in the file **page_frameset.html** the following code before the end of the `body` tag so to register the **Service Worker** of the webapp:
```

        <!- Service Worker registration -->
        ${pwa_service_worker}
    </body>

```



[Maven documentation and reports](http://dev.lutece.paris.fr/plugins/plugin-pwa/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*