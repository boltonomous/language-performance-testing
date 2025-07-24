#!/bin/bash
# Comprehensive performance test - Three complete runs
# Tests: compute, users (all), users (filtered)
# Load levels: Light (2K/10), Medium (10K/100), Heavy (100K/200)

echo "Performance Test Suite - Three Complete Runs"
echo "==========================================="
echo "Testing compute, users (all), and users (filtered) endpoints"
echo "Load levels: Light (2K/50), Medium (10K/100), Heavy (100K/200)"
echo ""

# Services to test
services="localhost:8080 localhost:8081 localhost:8082 localhost:8083 localhost:8084 localhost:8085 localhost:8086"
service_names=("Java Micronaut" "Kotlin Micronaut" "Kotlin Ktor" "Scala ZIO" "Go Gin" "Kotlin Spring Boot" "Scala Play")

# Create compute payload
cat > /tmp/compute_payload.json << EOF
{"numbers": [1, 2, 3, 4, 5], "operation": "sum"}
EOF

# Function to run a single test
run_test() {
    local endpoint=$1
    local requests=$2
    local concurrency=$3
    local service_url=$4
    local post_data=$5
    
    if [ -n "$post_data" ]; then
        ab -n "$requests" -c "$concurrency" -p "$post_data" -T "application/json" "$service_url" 2>&1 | \
            grep -E "(Requests per second|Failed requests|Time per request.*mean\))" | \
            head -3
    else
        ab -n "$requests" -c "$concurrency" "$service_url" 2>&1 | \
            grep -E "(Requests per second|Failed requests|Time per request.*mean\))" | \
            head -3
    fi
}

# Function to run all tests for a single run
run_complete_suite() {
    local run_number=$1
    echo "============================================"
    echo "RUN $run_number - Starting at $(date)"
    echo "============================================"
    
    # Test each endpoint type
    for endpoint_type in "compute" "users_all" "users_filtered"; do
        echo ""
        echo "--- Testing $endpoint_type endpoints ---"
        
        # Test each load level
        for load in "light" "medium" "heavy"; do
            case $load in
                "light")
                    requests=2000
                    concurrency=50
                    ;;
                "medium")
                    requests=10000
                    concurrency=100
                    ;;
                "heavy")
                    requests=100000
                    concurrency=200
                    ;;
            esac
            
            echo ""
            echo "=== $endpoint_type - $load load ($requests/$concurrency) ==="
            
            # Test each service
            i=0
            for service in $services; do
                echo ""
                echo "${service_names[$i]}:"
                
                case $endpoint_type in
                    "compute")
                        url="http://$service/api/compute"
                        run_test "$endpoint_type" "$requests" "$concurrency" "$url" "/tmp/compute_payload.json"
                        ;;
                    "users_all")
                        url="http://$service/api/users"
                        run_test "$endpoint_type" "$requests" "$concurrency" "$url" ""
                        ;;
                    "users_filtered")
                        url="http://$service/api/users?department=Engineering"
                        run_test "$endpoint_type" "$requests" "$concurrency" "$url" ""
                        ;;
                esac
                
                i=$((i + 1))
                
                # Small delay between services to avoid overwhelming
                sleep 1
            done
        done
    done
    
    echo ""
    echo "Run $run_number completed at $(date)"
    echo ""
}

# Main execution
echo "Starting performance tests..."
echo "Warming up services with a few requests first..."

# Warm up each service
for service in $services; do
    curl -s "http://$service/health" > /dev/null 2>&1
    curl -s -X POST "http://$service/api/compute" -H "Content-Type: application/json" -d '{"numbers":[1,2,3],"operation":"sum"}' > /dev/null 2>&1
    curl -s "http://$service/api/users" > /dev/null 2>&1
done

echo "Warm-up complete. Starting performance tests..."
echo ""

# Run three complete test suites
for run in 1 2 3; do
    run_complete_suite $run
    
    # Wait between runs
    if [ $run -lt 3 ]; then
        echo "Waiting 30 seconds before next run..."
        sleep 30
    fi
done

# Cleanup
rm -f /tmp/compute_payload.json

echo ""
echo "============================================"
echo "All performance tests completed!"
echo "============================================"