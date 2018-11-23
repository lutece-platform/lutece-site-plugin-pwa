const offlineFallback = "jsp/site/Portal.jsp?page=offline";
const shellCacheName = "Lutece PWA Plugin - ShellCache";
const shellCacheFiles = [offlineFallback, "css/bootstrap.min.css"];
const contentCacheName = "Lutece PWA Plugin - ContentCache";
const contentCacheFiles = ["css/bootstrap.min.css"];

// On install
self.addEventListener("install", event => {
  caches.open(contentCacheName).then(contentCache => {
    contentCache
      .addAll(contentCacheFiles)
      .then(() => {
        console.info(`[SW] content files added to ${contentCacheName}`);
      })
      .catch(error => {
        console.error(
          `[SW] Couldn't add content files to ${contentCacheName}: ${error}`
        );
      });
  });
  event.waitUntil(
    caches.open(shellCacheName).then(shellCache => {
      // These items must be cached for the Service Worker to complete installation
      let shellCachePromise = shellCache.addAll(shellCacheFiles).then(() => {
        console.info(`[SW] Shell files added to ${shellCacheName}`);
        console.info(`[SW] Service Worker installation done`);
      });
      shellCachePromise.catch(error => {
        console.error(
          `[SW] Couldn't add shell files to ${shellCacheName}: ${error}`
        );
      });
      return shellCachePromise;
    })
  );
});

// On activate
self.addEventListener("activate", event => {
  console.info(`[SW] Service Worker activated.`);
  event.waitUntil(clients.claim());
});

// On fetch
self.addEventListener("fetch", event => {
  const request = event.request;
  const requestUrl = new URL(request.url);

  console.groupCollapsed(`[SW] Page requests ${requestUrl.pathname}`);
  console.info(`- mode: ${request.mode}`);
  console.info(`- destination: ${request.destination}`);
  console.info(`- origin: ${requestUrl.origin}`);
  console.groupEnd();

  // don't mana cache for resources coming from another domain
  if (requestUrl.origin !== location.origin) {
    return;
  }

  if (
    /(\.html|\.jsp|\/)$/.test(requestUrl.pathname) ||
    request.destination === "image"
  ) {
    // Deal with HTML as Network First
    event.respondWith(
      fetch(request)
        .then(response => {
          // Resource provided by the server
          console.info(
            `[SW] Resource ${requestUrl.pathname} taken from server`
          );
          if (response.status === 200) {
            // Stash a copy of this page in the content cache
            // Only cache valid responses (prevent caching of 40x or 50x errors)
            let copy = response.clone();
            caches
              .open(contentCacheName)
              .then(cache => cache.put(request, copy));
          }
          return response;
        })
        .catch(() => {
          // Not available from the server, try from cache, or error
          return caches.match(request).then(response => {
            if (response) {
              // Resource provided by the cache
              console.info(
                `[SW] Resource ${requestUrl.pathname} taken from the cache`
              );
              return response;
            } else {
              // Resource not available, either in cache nor from server
              console.info(
                `[SW] Resource ${requestUrl.pathname} not available anywhere`
              );
              return caches.match(offlineFallback);
            }
          });
        })
    );
    return;
  } else {
    // Deal with non HTML or JSP as Cache First (ex : js, css, ...)
    event.respondWith(
      // https://mdn.io/cache+match
      caches.match(request).then(response => {
        // Resource available in cache
        if (response) {
          console.info(`[SW] Resource ${requestUrl.pathname} taken from cache`);
          return response;
        } else {
          return fetch(request)
            .then(response => {
              // Resource provided by the server
              if (response.status === 200) {
                // Stash a copy of this page in the pages cache
                // Only cache valid responses (prevent caching of 40x or 50x errors)
                let copy = response.clone();
                // https://mdn.io/cache+put
                caches
                  .open(shellCacheName)
                  .then(cache => cache.put(request, copy));
                console.info(
                  `[SW] Resource ${
                    requestUrl.pathname
                  } stored in cache "${shellCacheName}"`
                );
              }
              return response;
            })
            .catch(() => {
              // Resource not available, either in cache nor from server
              console.info(
                `[SW] Resource ${requestUrl.pathname} not available anywhere`
              );
              return new Response("resource not available", {
                status: 404,
                statusText: "Not found",
                contentType: "text/plain"
              });
            });
        }
      })
    );
  }
});
