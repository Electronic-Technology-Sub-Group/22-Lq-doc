一款用于同步文件的工具

```cardlink
url: https://github.com/syncthing/syncthing
title: "GitHub - syncthing/syncthing: Open Source Continuous File Synchronization"
description: "Open Source Continuous File Synchronization. Contribute to syncthing/syncthing development by creating an account on GitHub."
host: github.com
favicon: https://github.githubassets.com/favicons/favicon.svg
image: https://opengraph.githubassets.com/0b6e723fd6832fce4344a5f93360b1b35c73bfbd670802653fe63279a7d90f31/syncthing/syncthing
```

```yaml title:docker
version: "3"
services:
  syncthing:
    image: syncthing/syncthing
    container_name: syncthing
    hostname: my-syncthing
    environment:
      - PUID=1000
      - PGID=1000
    volumes:
      - /wherever/st-sync:/var/syncthing
    ports:
      - 8384:8384 # Web UI
      - 22000:22000/tcp # TCP file transfers
      - 22000:22000/udp # QUIC file transfers
      - 21027:21027/udp # Receive local discovery broadcasts
    restart: unless-stopped
```

‍
