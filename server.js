const express = require('express');
const fs = require('fs');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

const APK_PATH = path.join(__dirname, 'app', 'build', 'outputs', 'apk', 'debug', 'app-debug.apk');

app.get('/download-apk', (req, res) => {
  if (fs.existsSync(APK_PATH)) {
    res.download(APK_PATH, 'vanguard-wardrobe-debug.apk', (err) => {
      if (err) {
        console.error('Error sending APK:', err);
        if (!res.headersSent) {
          res.status(500).send('Error downloading APK.');
        }
      }
    });
  } else {
    res.status(404).send('APK not found. The Android app is currently building or has not been compiled yet.');
  }
});

app.get('/api/status', (req, res) => {
  const exists = fs.existsSync(APK_PATH);
  let sizeBytes = 0;
  let formattedSize = '0 MB';
  let mtime = null;

  if (exists) {
    const stats = fs.statSync(APK_PATH);
    sizeBytes = stats.size;
    formattedSize = (sizeBytes / (1024 * 1024)).toFixed(2) + ' MB';
    mtime = stats.mtime;
  }

  res.json({
    appName: 'Vanguard Wardrobe',
    phase: 'Phase 1 — Wardrobe & Style Foundation',
    targetSdk: 36,
    minSdk: 24,
    apkReady: exists,
    apkSize: formattedSize,
    lastBuilt: mtime,
  });
});

app.get('/', (req, res) => {
  const exists = fs.existsSync(APK_PATH);
  let apkSize = 'Pending compilation';
  if (exists) {
    const stats = fs.statSync(APK_PATH);
    apkSize = (stats.size / (1024 * 1024)).toFixed(2) + ' MB';
  }

  const html = `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Vanguard Wardrobe — AI Style System</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&family=Cinzel:wght@600;700&display=swap" rel="stylesheet">
  <style>
    :root {
      --bg: #0b0d10;
      --card-bg: #13171d;
      --card-border: #222933;
      --text: #f0f3f6;
      --text-muted: #8c9ba8;
      --accent: #c5a059;
      --accent-glow: rgba(197, 160, 89, 0.2);
      --success: #22c55e;
      --font-display: 'Cinzel', serif;
      --font-body: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif;
    }
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      background-color: var(--bg);
      color: var(--text);
      font-family: var(--font-body);
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 24px;
      line-height: 1.5;
    }
    .container {
      max-width: 680px;
      width: 100%;
      background: var(--card-bg);
      border: 1px solid var(--card-border);
      border-radius: 20px;
      padding: 40px;
      box-shadow: 0 20px 40px rgba(0, 0, 0, 0.6);
      position: relative;
      overflow: hidden;
    }
    .container::before {
      content: '';
      position: absolute;
      top: 0; left: 0; right: 0; height: 3px;
      background: linear-gradient(90deg, #c5a059, #e5c988, #c5a059);
    }
    .badge {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      padding: 6px 14px;
      border-radius: 999px;
      background: rgba(197, 160, 89, 0.12);
      border: 1px solid rgba(197, 160, 89, 0.3);
      color: var(--accent);
      font-size: 11px;
      font-weight: 700;
      letter-spacing: 1.5px;
      text-transform: uppercase;
      margin-bottom: 20px;
    }
    .badge-dot {
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background: var(--accent);
    }
    h1 {
      font-family: var(--font-display);
      font-size: 32px;
      font-weight: 700;
      letter-spacing: 0.5px;
      margin-bottom: 8px;
      color: #ffffff;
    }
    p.subtitle {
      color: var(--text-muted);
      font-size: 15px;
      margin-bottom: 32px;
    }
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 16px;
      margin-bottom: 32px;
    }
    .stat-box {
      background: rgba(255, 255, 255, 0.02);
      border: 1px solid var(--card-border);
      border-radius: 12px;
      padding: 16px;
      text-align: center;
    }
    .stat-label {
      font-size: 11px;
      text-transform: uppercase;
      letter-spacing: 1px;
      color: var(--text-muted);
      margin-bottom: 6px;
    }
    .stat-val {
      font-size: 17px;
      font-weight: 700;
      color: #ffffff;
    }
    .features-list {
      background: rgba(0, 0, 0, 0.25);
      border: 1px solid var(--card-border);
      border-radius: 12px;
      padding: 20px;
      margin-bottom: 32px;
    }
    .features-title {
      font-size: 13px;
      text-transform: uppercase;
      letter-spacing: 1px;
      color: var(--accent);
      font-weight: 700;
      margin-bottom: 12px;
    }
    .features-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 10px;
      font-size: 13px;
      color: #cbd5e1;
    }
    .feature-item {
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .check {
      color: var(--accent);
      font-weight: bold;
    }
    .btn-download {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      width: 100%;
      background: linear-gradient(135deg, #c5a059 0%, #a6823c 100%);
      color: #0b0d10;
      padding: 16px 24px;
      border-radius: 12px;
      text-decoration: none;
      font-weight: 700;
      font-size: 15px;
      letter-spacing: 0.5px;
      box-shadow: 0 8px 20px var(--accent-glow);
      transition: all 0.2s ease;
      cursor: pointer;
      border: none;
    }
    .btn-download:hover {
      transform: translateY(-2px);
      box-shadow: 0 12px 28px rgba(197, 160, 89, 0.35);
    }
    .btn-download svg {
      width: 20px;
      height: 20px;
    }
    .footer-note {
      text-align: center;
      margin-top: 18px;
      font-size: 12px;
      color: var(--text-muted);
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="badge">
      <span class="badge-dot"></span>
      Phase 1 Native Engine
    </div>
    <h1>VANGUARD WARDROBE</h1>
    <p class="subtitle">Premium AI-ready personal wardrobe catalog, style profile architect, and luxury fashion intelligence foundation.</p>

    <div class="stats-grid">
      <div class="stat-box">
        <div class="stat-label">Build Status</div>
        <div class="stat-val" style="color: ${exists ? 'var(--success)' : '#eab308'}">
          ${exists ? 'Ready' : 'Building'}
        </div>
      </div>
      <div class="stat-box">
        <div class="stat-label">APK Package Size</div>
        <div class="stat-val">${apkSize}</div>
      </div>
      <div class="stat-box">
        <div class="stat-label">Target OS</div>
        <div class="stat-val">Android 15 (API 36)</div>
      </div>
    </div>

    <div class="features-list">
      <div class="features-title">Core Phase 1 Architecture</div>
      <div class="features-grid">
        <div class="feature-item"><span class="check">✓</span> 12 Category Structured System</div>
        <div class="feature-item"><span class="check">✓</span> Reactive Room DB Persistence</div>
        <div class="feature-item"><span class="check">✓</span> Multi-facet Filters & Live Search</div>
        <div class="feature-item"><span class="check">✓</span> Style Aesthetics & Personal Rules</div>
        <div class="feature-item"><span class="check">✓</span> Image Upload & Photo Selection</div>
        <div class="feature-item"><span class="check">✓</span> Future AI Stylist Extension Hooks</div>
      </div>
    </div>

    <a href="/download-apk" class="btn-download">
      <svg fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" d="M3 16.5v2.25A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75V16.5M7.5 12L12 16.5m0 0L16.5 12M12 16.5V3"></path>
      </svg>
      Download APK Directly
    </a>
    <p class="footer-note">Compiled for high-performance Jetpack Compose on modern Android devices.</p>
  </div>
</body>
</html>`;

  res.send(html);
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Vanguard companion server listening on port ${PORT}`);
});
