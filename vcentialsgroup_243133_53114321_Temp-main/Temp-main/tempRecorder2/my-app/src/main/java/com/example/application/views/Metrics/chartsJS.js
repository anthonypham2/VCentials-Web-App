const ctx1 = document.getElementById("chart-1");

new Chart(ctx1, {
  type: 'polarArea',
  data: {
    labels: ['West Campus', 'East Campus', 'Osceola Campus'],
    datasets: [{
      label: 'Weekly Average',
      data: [35, 37, 38],

    }]
  },
  options: {
    responsive: true,
  }
});

const ctx2 = document.getElementById("chart-2");

new Chart(ctx2, {
  type: 'bar',
  data: {
    labels: ['West Campus', 'East Campus', 'Osceola Campus'],
    datasets: [{
      label: 'Temperature',
      data: [35, 37, 38],
      backgroundColor: [
        "rgba(54, 162, 235, 1)",
        "rgba(255, 99, 132, 1)",
        "rgba(255, 206, 86, 1)",
      ],
    }]
},
options: {
  responsive: true,
}
});
