import http from 'k6/http';

export const options = {
  vus: 10,
 iterations: 10000,
 summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)']
};

 

export default function () {
  const url =
    'http://localhost:8080/api/v1/loadComment' +
    '?limit=4' +
    '&dayAgo=0';

  const params = {
    headers: {
      Authorization:
        'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNzcwNDQxNTkyLCJleHAiOjE3NzA1NDE1OTJ9.lR_ybv1otM9Xce8quHdSz5WQhqd_SNwFPdzOnlvO6Fg',
    },
  };

  const res = http.get(url, params);
}