	import http from 'k6/http';

export const options = {
  vus: 10,
 iterations: 10000,
 summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)']
};

const MAX_ID = 1_000_000;

export default function () {
  const lastId = Math.floor(Math.random() * MAX_ID) + 1;
  http.get(`http://localhost:8080/api/v1/loadComment?limit=1&lastId=${lastId}`);
}