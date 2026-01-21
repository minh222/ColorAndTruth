import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 1,
  duration: '5s',
  discardResponseBodies: true,
};

export default function () {
  http.post(
    'http://localhost:8080/api/v1/analyze?original=warm'
  );
  sleep(0.1);
}
