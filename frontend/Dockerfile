FROM node:22-alpine
WORKDIR /app

ENV NPM_CONFIG_CACHE=/tmp/.npm

COPY package*.json ./
RUN npm install -g npm@11.12.1 \
    && npm cache clean --force \
    && npm ci --legacy-peer-deps --no-audit --no-fund

COPY . .
RUN npm run build

ENV NODE_ENV=production
ENV PORT=4000

EXPOSE 4000
CMD ["node", "dist/estudiomusical-frontend/server/server.mjs"]
