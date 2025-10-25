import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = 'http://localhost:8080/api/v1/users';

// Load test configuration
export const options = {
    stages: [
        { duration: '1m', target: 100 },   // 100 users
        { duration: '2m', target: 200 },   // 200 users
        { duration: '1m', target: 0 },
    ],
};

export default function() {
    const user = {
        name: `User${Date.now()}`,
        email: `user${Date.now()}@test.com`
    };

    // CREATE
    let res = http.post(BASE_URL, JSON.stringify(user), {
        headers: { 'Content-Type': 'application/json' }
    });
    check(res, { 'create: status 200/201': (r) => r.status === 200 || r.status === 201 });

    const userId = JSON.parse(res.body).id;
    sleep(0.5);

    // READ
    res = http.get(`${BASE_URL}/${userId}`);
    check(res, { 'read: status 200': (r) => r.status === 200 });
    sleep(0.5);

    // UPDATE
    user.name = `${user.name}_updated`;
    res = http.put(`${BASE_URL}/${userId}`, JSON.stringify(user), {
        headers: { 'Content-Type': 'application/json' }
    });
    check(res, { 'update: status 200': (r) => r.status === 200 });
    sleep(0.5);

    // DELETE
    res = http.del(`${BASE_URL}/${userId}`);
    check(res, { 'delete: status 200': (r) => r.status === 200 });
    sleep(1);
}
//
// > k6 run .\k6\load-test-crud.js
//
// /\      Grafana   /‾‾/
// /\  /  \     |\  __   /  /
// /  \/    \    | |/ /  /   ‾‾\
//   /          \   |   (  |  (‾)  |
// / __________ \  |_|\_\  \_____/
//
// execution: local
// script: .\k6\load-test-crud.js
// output: -
//
//     scenarios: (100.00%) 1 scenario, 200 max VUs, 4m30s max duration (incl. graceful stop):
// * default: Up to 200 looping VUs for 4m0s over 3 stages (gracefulRampDown: 30s, gracefulStop: 30s)
//
// WARN[0107] Request Failed                                error="Post \"http://localhost:8080/api/v1/users\": request timeout"
// ERRO[0107] TypeError: Cannot read property 'id' of undefined
// running at default (file:///D:/VK/workspace/JAVA/java-fullstack/k6/load-test-crud.js:27:41(45))  executor=ramping-vus scenario=default source=stacktrace
//     WARN[0112] The test has generated metrics with 100045 unique time series, which is higher than the suggested limit of 100000 and could cause high memory usage. Consider not using high-cardinality values like unique IDs as metric tags or, if you need them in the URL, use the name metric tag or URL grouping. See https://grafana.com/docs/k6/latest/using-k6/tags-and-groups/ for details.  component=metrics-engine-ingester
//     WARN[0113] Request Failed                                error="Post \"http://localhost:8080/api/v1/users\": request timeout"
// ERRO[0113] TypeError: Cannot read property 'id' of undefined
// running at default (file:///D:/VK/workspace/JAVA/java-fullstack/k6/load-test-crud.js:27:41(45))  executor=ramping-vus scenario=default source=stacktrace
//     WARN[0146] Request Failed                                error="Get \"http://localhost:8080/api/v1/users/3631\": request timeout"
// WARN[0168] The test has generated metrics with 200134 unique time series, which is higher than the suggested limit of 100000 and could cause high memory usage. Consider not using high-cardinality values like unique IDs as metric tags or, if you need them in the URL, use the name metric tag or URL grouping. See https://grafana.com/docs/k6/latest/using-k6/tags-and-groups/ for details.  component=metrics-engine-ingester
//     WARN[0199] Request Failed                                error="Get \"http://localhost:8080/api/v1/users/6683\": request timeout"
// WARN[0207] Request Failed                                error="Put \"http://localhost:8080/api/v1/users/7166\": request timeout"
// WARN[0222] Request Failed                                error="Delete \"http://localhost:8080/api/v1/users/8190\": request timeout"
// WARN[0228] Request Failed                                error="Get \"http://localhost:8080/api/v1/users/8731\": request timeout"
//
//
//   █ TOTAL RESULTS
//
// checks_total.......: 42761  170.906654/s
// checks_succeeded...: 99.98% 42754 out of 42761
// checks_failed......: 0.01%  7 out of 42761
//
//     ✗ create: status 200/201
//       ↳  99% — ✓ 10691 / ✗ 2
//     ✗ read: status 200
//       ↳  99% — ✓ 10687 / ✗ 3
//     ✗ update: status 200
//       ↳  99% — ✓ 10688 / ✗ 1
//     ✗ delete: status 200
//       ↳  99% — ✓ 10688 / ✗ 1
//
// HTTP
// http_req_duration..............: avg=10.35ms min=0s   med=535.5µs max=1m0s    p(90)=1ms  p(95)=1.19ms
// { expected_response:true }...: avg=538µs   min=0s   med=535.5µs max=27.71ms p(90)=1ms  p(95)=1.19ms
// http_req_failed................: 0.01%  7 out of 42761
// http_reqs......................: 42761  170.906654/s
//
// EXECUTION
// iteration_duration.............: avg=2.54s   min=2.5s med=2.5s    max=1m2s    p(90)=2.5s p(95)=2.5s
// iterations.....................: 10691  42.729661/s
// vus............................: 1      min=1          max=200
// vus_max........................: 200    min=200        max=200
//
// NETWORK
// data_received..................: 5.8 MB 23 kB/s
// data_sent......................: 6.3 MB 25 kB/s
//
//
//
//
// running (4m10.2s), 000/200 VUs, 10691 complete and 3 interrupted iterations
// default ✓ [======================================] 000/200 VUs  4m0s


// Performance Results:
//
// 99.98% success rate at 200 concurrent users
// 170 requests/second throughput
// 538μs avg response time for successful requests
// Bottleneck appears around: ~170-200 concurrent users
