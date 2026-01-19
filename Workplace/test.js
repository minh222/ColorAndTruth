import http from 'k6/http';

export const options = {
  vus: 50,
  duration: '2m',
 summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)']
};

export default function () {
  const url =
    'http://localhost:8080/api/v1/analyze' +
    '?original=' +
    encodeURIComponent('Tao mừng cho mày mà cay vl');

  const res = http.post(url);

  console.log('status =', res.status);
  console.log('body =', res.body);
}
