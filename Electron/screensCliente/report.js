const data = [
  {
    folder: "2026",
    reports: [
      { title: "Revisión general", date: "10/01/2026" },
      { title: "Diagnóstico electrónico", date: "05/01/2026" }
    ]
  },
  {
    folder: "2025",
    reports: [
      { title: "Informe de frenos", date: "02/11/2025" },
      { title: "Cambio de aceite", date: "12/08/2025" }
    ]
  }
];

const wrapper = document.getElementById("foldersWrapper");

data.forEach((group, index) => {
  const card = document.createElement("div");
  card.className = "car-card folder-card";

  card.innerHTML = `
    <div class="folder-tab">
        <div class="folder-title">📁 ${group.folder}</div>
        <div class="folder-arrow">▶</div>
    </div>

    <div class="folder-content"></div>
  `;

  const content = card.querySelector(".folder-content");

  group.reports.forEach((report, i) => {
    const file = document.createElement("div");
    file.className = "file-item";
    file.innerHTML = `📄 ${report.title} <span>${report.date}</span>`;

    file.addEventListener("click", (e) => {
      e.stopPropagation();
      // location.href = `details.html?id=${index}_${i}`;
      alert("Abrir informe: " + report.title);
    });

    content.appendChild(file);
  });

  const tab = card.querySelector(".folder-tab");
  const arrow = card.querySelector(".folder-arrow");

  tab.addEventListener("click", () => {
    content.classList.toggle("open");
    arrow.classList.toggle("open");
  });

  wrapper.appendChild(card);
});
