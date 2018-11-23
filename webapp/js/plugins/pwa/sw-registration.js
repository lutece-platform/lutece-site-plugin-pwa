if ("serviceWorker" in navigator) {
  console.info("[page] Your browser supports Service Workers.");
  window.addEventListener("load", () => {
    navigator.serviceWorker
      .register("./service-worker.js", { scope: "./" })
      .then(registration => {
        console.info(
          "[page] Service Worker registration successful with scope: ",
          registration.scope
        );
      })
      .catch(err => {
        console.error("[page] ServiceWorker registration failed: ", err);
      });
  });
} else {
  console.error("[page] Your browser doesn't support Service Workers");
}
