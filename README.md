# epicraft-pdp

Monorepo for invoice management with:
- `invoice-api`: Spring Boot + MongoDB backend
- `invoice-front`: Angular 20 frontend

## Project structure

- `invoice-api/` backend REST API for companies, customers, and invoices.
- `invoice-front/` frontend app that consumes `invoice-api`.
- `docker-compose.yml` local multi-service stack.

## Run with Docker Compose

```bash
git submodule update --init --recursive
docker compose up --build
```

Services:
- Frontend (includes `/api` reverse proxy to backend): `http://localhost:4200`
- API (direct access): `http://localhost:8080`
- MongoDB: `mongodb://localhost:27017`

The backend container connects to MongoDB with the Compose service hostname
`mongodb` on the shared `invoice-net` network. If you run the API outside
Docker Compose, keep using `mongodb://localhost:27017/invoice_api` instead;
`mongodb` only resolves inside the Compose network.

If MongoDB exits immediately with `exec format error`, Docker is using an
incompatible cached image for your machine. The Compose file pins MongoDB to
`linux/amd64` and pulls the image on each `docker compose up`; you can also
refresh it manually with:

```bash
docker compose pull mongodb
docker compose up --force-recreate mongodb
```


### Docker BuildKit storage error

If Docker Compose fails while building `invoice-front` with an error similar to:

```text
target invoice-front: failed to solve: error committing ... metadata_v2.db: read-only file system
```

that message comes from Docker BuildKit writing to Docker's host-side storage
(`/var/lib/docker/buildkit/...`), not from the Angular app or this Dockerfile.
The usual fix is to restore Docker's writable storage and then rerun the build:

```bash
docker info
sudo mount -o remount,rw /var/lib/docker  # only if /var/lib/docker is mounted read-only
sudo systemctl restart docker             # Linux hosts with systemd
docker builder prune --filter type=exec.cachemount
COMPOSE_BAKE=false docker compose build --no-cache invoice-front
```

On Docker Desktop, restart Docker Desktop and run **Troubleshoot → Clean / Purge
data** if the BuildKit store remains read-only or corrupted. After Docker is
healthy, rebuild the stack with `docker compose up --build`.

## Development notes

