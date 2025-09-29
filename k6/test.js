import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 50,
    // duration: '10s',
    iterations: 5000
};

export default function () {
    // Test different endpoints
    // const baseUrl = 'http://localhost:8080';
    const baseUrl = 'http://localhost:8081';

    // GET /
    let response = http.get(`${baseUrl}/`);
    check(response, {
        'homepage status is 200': (r) => r.status === 200,
    });

    // GET /api/time
    response = http.get(`${baseUrl}/api/time`);
    check(response, {
        'api/time status is 200': (r) => r.status === 200,
        'api/time returns JSON': (r) => r.headers['Content-Type'].includes('application/json'),
    });

    // Small pause between iterations
    sleep(0.1);
}

// Virtual threads
//
// $ k6 run k6/test.js
//
// /\      Grafana   /‾‾/
// /\  /  \     |\  __   /  /
// /  \/    \    | |/ /  /   ‾‾\
//   /          \   |   (  |  (‾)  |
// / __________ \  |_|\_\  \_____/
//
// execution: local
// script: k6/test.js
// output: -
//
//     scenarios: (100.00%) 1 scenario, 50 max VUs, 10m30s max duration (incl. graceful stop):
// * default: 5000 iterations shared among 50 VUs (maxDuration: 10m0s, gracefulStop: 30s)
//
//
//
// █ TOTAL RESULTS
//
// checks_total.......: 15000   972.503867/s
// checks_succeeded...: 100.00% 15000 out of 15000
// checks_failed......: 0.00%   0 out of 15000
//
//     ✓ homepage status is 200
//     ✓ api/time status is 200
//     ✓ api/time returns JSON
//
// HTTP
// http_req_duration..............: avg=21.68ms  min=0s      med=663µs    max=1.03s p(90)=1.86ms   p(95)=3.2ms
// { expected_response:true }...: avg=21.68ms  min=0s      med=663µs    max=1.03s p(90)=1.86ms   p(95)=3.2ms
// http_req_failed................: 0.00%  0 out of 10000
// http_reqs......................: 10000  648.335911/s
//
// EXECUTION
// iteration_duration.............: avg=145.66ms min=100.8ms med=103.29ms max=1.15s p(90)=106.64ms p(95)=131.69ms
// iterations.....................: 5000   324.167956/s
// vus............................: 8      min=8          max=50
// vus_max........................: 50     min=50         max=50
//
// NETWORK
// data_received..................: 5.0 MB 322 kB/s
// data_sent......................: 750 kB 49 kB/s
//
//
//
//
// running (00m15.4s), 00/50 VUs, 5000 complete and 0 interrupted iterations
// default ✓ [======================================] 50 VUs  00m15.4s/10m0s  5000/5000 shared iters


// Platform threads
//
// $ k6 run k6/test.js
//
// /\      Grafana   /‾‾/
// /\  /  \     |\  __   /  /
// /  \/    \    | |/ /  /   ‾‾\
//   /          \   |   (  |  (‾)  |
// / __________ \  |_|\_\  \_____/
//
// execution: local
// script: k6/test.js
// output: -
//
//     scenarios: (100.00%) 1 scenario, 50 max VUs, 10m30s max duration (incl. graceful stop):
// * default: 5000 iterations shared among 50 VUs (maxDuration: 10m0s, gracefulStop: 30s)
//
//
//
// █ TOTAL RESULTS
//
// checks_total.......: 15000   1043.891359/s
// checks_succeeded...: 100.00% 15000 out of 15000
// checks_failed......: 0.00%   0 out of 15000
//
//     ✓ homepage status is 200
//     ✓ api/time status is 200
//     ✓ api/time returns JSON
//
// HTTP
// http_req_duration..............: avg=16.2ms   min=0s       med=610.5µs  max=1.01s p(90)=2ms      p(95)=4.01ms
// { expected_response:true }...: avg=16.2ms   min=0s       med=610.5µs  max=1.01s p(90)=2ms      p(95)=4.01ms
// http_req_failed................: 0.00%  0 out of 10000
// http_reqs......................: 10000  695.927572/s
//
// EXECUTION
// iteration_duration.............: avg=134.79ms min=100.83ms med=103.19ms max=1.11s p(90)=107.83ms p(95)=119.04ms
// iterations.....................: 5000   347.963786/s
// vus............................: 7      min=7          max=50
// vus_max........................: 50     min=50         max=50
//
// NETWORK
// data_received..................: 5.0 MB 345 kB/s
// data_sent......................: 749 kB 52 kB/s
//
//
//
//
// running (00m14.4s), 00/50 VUs, 5000 complete and 0 interrupted iterations
// default ✓ [======================================] 50 VUs  00m14.4s/10m0s  5000/5000 shared iters