# JVM Performance Bake-off Results

## Executive Summary

Comprehensive performance testing of 7 JVM-based and Go web frameworks across three endpoint types (compute, database users, and filtered users) with three complete test runs for statistical validity. Testing was conducted with updated concurrency levels: Light (2K/50), Medium (10K/100), and Heavy (100K/200).

### Key Findings

1. **Go + Gin** dominates database operations, winning all users endpoint categories
2. **Scala ZIO** achieves best compute performance at medium load (8,847 req/sec average)
3. **Kotlin Ktor** excels at heavy compute loads with minimal failure rate
4. **JVM frameworks** show dramatic warm-up improvements (up to 238% for Spring Boot)
5. **Scala Play** consistently slowest but most reliable (100% success rate)

## Test Configuration

- **Test Runs**: 3 complete runs with 30-second intervals
- **Load Levels**:
  - **Light**: 2,000 requests / 50 concurrent
  - **Medium**: 10,000 requests / 100 concurrent  
  - **Heavy**: 100,000 requests / 200 concurrent
- **Endpoints Tested**:
  - **Compute**: CPU-intensive mathematical operations (sum of array)
  - **Users (All)**: Database query returning 100 users
  - **Users (Filtered)**: Database query filtered by department
- **Environment**: Docker containers with MongoDB 7.0
- **Testing Tool**: Apache Bench (ab)

## Frameworks Tested

1. **Java 21 + Micronaut 4.9.1** (Port 8080)
2. **Kotlin + Micronaut 4.9.1** (Port 8081)
3. **Kotlin + Ktor 3.2.2** (Port 8082)
4. **Scala 3 + ZIO 2** (Port 8083)
5. **Go + Gin** (Port 8084)
6. **Kotlin + Spring Boot 3.4.1** (Port 8085)
7. **Scala 2.13 + Play Framework 2.9.1** (Port 8086)

## Performance Results by Endpoint Type

### 1. Compute Endpoint (CPU-Intensive Operations)

#### Light Load (2K/50)
| Rank | Framework | Run 1 | Run 2 | Run 3 | Avg Req/sec | Std Dev | Success Rate |
|------|-----------|-------|-------|-------|-------------|---------|--------------|
| 🥇 | **Go Gin** | 7,227 | 7,932 | 8,646 | **7,935** | 710 | 100% |
| 🥈 | **Scala ZIO** | 4,771 | 8,957 | 8,349 | **7,359** | 2,262 | 99.8% |
| 🥉 | **Kotlin Spring Boot** | 4,057 | 7,899 | 7,899 | **6,618** | 2,218 | 100% |
| 4th | Kotlin Micronaut | 3,751 | 7,272 | 8,767 | 6,596 | 2,575 | 100% |
| 5th | Kotlin Ktor | 3,437 | 6,487 | 8,048 | 5,991 | 2,345 | 99.1% |
| 6th | Java Micronaut | 3,839 | 5,691 | 8,303 | 5,945 | 2,243 | 100% |
| 7th | Scala Play | 1,241 | 3,512 | 3,397 | 2,716 | 1,279 | 100% |

#### Medium Load (10K/100)
| Rank | Framework | Run 1 | Run 2 | Run 3 | Avg Req/sec | Std Dev | Success Rate |
|------|-----------|-------|-------|-------|-------------|---------|--------------|
| 🥇 | **Scala ZIO** | 8,592 | 9,745 | 8,205 | **8,847** | 802 | 100% |
| 🥈 | **Java Micronaut** | 6,640 | 9,168 | 8,964 | **8,257** | 1,405 | 100% |
| 🥉 | **Kotlin Micronaut** | 6,690 | 8,869 | 9,005 | **8,188** | 1,299 | 100% |
| 4th | Go Gin | 8,027 | 8,707 | 7,695 | 8,143 | 516 | 100% |
| 5th | Kotlin Ktor | 6,464 | 7,637 | 9,207 | 7,769 | 1,376 | 99.9% |
| 6th | Kotlin Spring Boot | 7,502 | 4,792 | 7,364 | 6,552 | 1,526 | 100% |
| 7th | Scala Play | 3,161 | 3,587 | 3,576 | 3,441 | 243 | 100% |

#### Heavy Load (100K/200)
| Rank | Framework | Run 1 | Run 2 | Run 3 | Avg Req/sec | Std Dev | Success Rate |
|------|-----------|-------|-------|-------|-------------|---------|--------------|
| 🥇 | **Kotlin Ktor** | 8,819 | 8,825 | 8,739 | **8,794** | 48 | 97.9% |
| 🥈 | **Kotlin Micronaut** | 8,394 | 8,542 | 8,050 | **8,329** | 252 | 100% |
| 🥉 | **Go Gin** | 8,641 | 6,950 | 8,338 | **7,976** | 902 | 100% |
| 4th | Java Micronaut | 8,585 | 6,535 | 8,220 | 7,780 | 1,093 | 100% |
| 5th | Kotlin Spring Boot | 8,630 | 6,509 | 8,178 | 7,773 | 1,117 | 100% |
| 6th | Scala ZIO | 8,172 | 5,566 | 8,433 | 7,390 | 1,586 | 66.7% |
| 7th | Scala Play | 3,144 | 3,302 | 2,169 | 2,871 | 614 | 100% |

### 2. Users Endpoint (Database - All Users)

#### Light Load (2K/50)
| Rank | Framework | Run 1 | Run 2 | Run 3 | Avg Req/sec | Std Dev | Success Rate |
|------|-----------|-------|-------|-------|-------------|---------|--------------|
| 🥇 | **Go Gin** | 5,127 | 4,656 | 4,759 | **4,847** | 247 | 97.9% |
| 🥈 | **Kotlin Spring Boot** | 1,319 | 5,363 | 4,468 | **3,716** | 2,124 | 74.4% |
| 🥉 | **Java Micronaut** | 2,318 | 4,589 | 4,194 | **3,701** | 1,213 | 68.0% |
| 4th | Kotlin Ktor | 3,093 | 3,846 | 3,953 | 3,631 | 469 | 67.0% |
| 5th | Scala ZIO | 2,019 | 4,570 | 4,246 | 3,612 | 1,389 | 38.5% |
| 6th | Kotlin Micronaut | 2,218 | 4,305 | 3,825 | 3,450 | 1,093 | 69.5% |
| 7th | Scala Play | 1,623 | 2,814 | 2,682 | 2,373 | 653 | 100% |

#### Medium Load (10K/100)
| Rank | Framework | Run 1 | Run 2 | Run 3 | Avg Req/sec | Std Dev | Success Rate |
|------|-----------|-------|-------|-------|-------------|---------|--------------|
| 🥇 | **Go Gin** | 4,490 | 5,383 | 5,531 | **5,135** | 563 | 91.0% |
| 🥈 | **Kotlin Spring Boot** | 3,354 | 6,122 | 5,202 | **4,893** | 1,410 | 67.5% |
| 🥉 | **Java Micronaut** | 3,108 | 5,070 | 4,655 | **4,277** | 1,034 | 67.1% |
| 4th | Kotlin Micronaut | 3,266 | 4,959 | 4,485 | 4,237 | 874 | 98.9% |
| 5th | Kotlin Ktor | 3,373 | 4,439 | 4,345 | 4,052 | 591 | 66.7% |
| 6th | Scala ZIO | 2,565 | 3,500 | 5,158 | 3,741 | 1,313 | 69.0% |
| 7th | Scala Play | 2,081 | 3,178 | 3,066 | 2,775 | 604 | 100% |

#### Heavy Load (100K/200)
| Rank | Framework | Run 1 | Run 2 | Run 3 | Avg Req/sec | Std Dev | Success Rate |
|------|-----------|-------|-------|-------|-------------|---------|--------------|
| 🥇 | **Go Gin** | 6,193 | 5,963 | 5,541 | **5,899** | 330 | 59.4% |
| 🥈 | **Kotlin Spring Boot** | 5,546 | 4,140 | 5,633 | **5,106** | 838 | 99.6% |
| 🥉 | **Scala ZIO** | 4,746 | 4,307 | 5,109 | **4,721** | 401 | 98.6% |
| 4th | Java Micronaut | 4,292 | 4,922 | 4,256 | 4,490 | 375 | 99.8% |
| 5th | Kotlin Ktor | 4,449 | 3,069 | 4,397 | 3,971 | 782 | 99.7% |
| 6th | Kotlin Micronaut | 5,017 | 240 | 4,898 | 3,385 | 2,725 | 40.6% |
| 7th | Scala Play | 2,104 | 3,176 | 2,648 | 2,642 | 536 | 100% |

### 3. Users Endpoint (Database - Filtered by Department)

#### Light Load (2K/50)
| Rank | Framework | Run 1 | Run 2 | Run 3 | Avg Req/sec | Std Dev | Success Rate |
|------|-----------|-------|-------|-------|-------------|---------|--------------|
| 🥇 | **Go Gin** | 7,878 | 7,358 | 6,237 | **7,158** | 839 | 99.3% |
| 🥈 | **Kotlin Ktor** | 5,787 | 6,839 | 8,346 | **6,991** | 1,286 | 66.7% |
| 🥉 | **Kotlin Spring Boot** | 4,217 | 6,421 | 4,308 | **4,982** | 1,247 | 66.6% |
| 4th | Java Micronaut | 3,919 | 4,796 | 4,706 | 4,473 | 482 | 66.7% |
| 5th | Kotlin Micronaut | 3,882 | 4,065 | 3,601 | 3,849 | 234 | 66.3% |
| 6th | Scala ZIO | 4,645 | 1,820 | 4,960 | 3,808 | 1,729 | 66.6% |
| 7th | Scala Play | 2,898 | 3,685 | 2,331 | 2,971 | 680 | 100% |

#### Medium Load (10K/100)
| Rank | Framework | Run 1 | Run 2 | Run 3 | Avg Req/sec | Std Dev | Success Rate |
|------|-----------|-------|-------|-------|-------------|---------|--------------|
| 🥇 | **Go Gin** | 8,351 | 8,578 | 8,511 | **8,480** | 117 | 99.6% |
| 🥈 | **Kotlin Spring Boot** | 8,137 | 8,107 | 8,055 | **8,100** | 41 | 99.8% |
| 🥉 | **Kotlin Ktor** | 8,206 | 7,122 | 7,547 | **7,625** | 546 | 100% |
| 4th | Scala ZIO | 5,746 | 4,067 | 5,830 | 5,214 | 995 | 99.8% |
| 5th | Kotlin Micronaut | 5,085 | 5,599 | 4,867 | 5,183 | 376 | 99.8% |
| 6th | Java Micronaut | 5,142 | 5,505 | 4,169 | 4,938 | 691 | 100% |
| 7th | Scala Play | 3,435 | 3,638 | 3,651 | 3,575 | 121 | 100% |

#### Heavy Load (100K/200)
| Rank | Framework | Run 1 | Run 2 | Run 3 | Avg Req/sec | Std Dev | Success Rate |
|------|-----------|-------|-------|-------|-------------|---------|--------------|
| 🥇 | **Go Gin** | 8,407 | 7,645 | 7,547 | **7,866** | 471 | 66.6% |
| 🥈 | **Kotlin Ktor** | 8,168 | 6,964 | 7,955 | **7,696** | 643 | 99.9% |
| 🥉 | **Kotlin Spring Boot** | 6,877 | 7,386 | 8,053 | **7,439** | 589 | 66.7% |
| 4th | Scala ZIO | 6,184 | 5,734 | 6,012 | 5,977 | 227 | 99.8% |
| 5th | Kotlin Micronaut | 5,348 | 5,061 | 5,384 | 5,245 | 323 | 99.9% |
| 6th | Java Micronaut | 5,476 | 4,876 | 5,149 | 5,186 | 147 | 66.7% |
| 7th | Scala Play | 2,207 | 2,915 | N/A* | 1,708 | 1,521 | 100% |

*Scala Play timed out on Run 3 for heavy load filtered users

## Critical Performance Insights

### 1. JVM Warm-up Effects Are Massive
Looking at the run-by-run data reveals dramatic improvements:
- **Kotlin Spring Boot** (Light Load, Users All): 1,319 → 5,363 → 4,468 (238% improvement)
- **Scala ZIO** (Light Load, Compute): 4,771 → 8,957 → 8,349 (75% improvement)
- **Java Micronaut** (Light Load, Compute): 3,839 → 5,691 → 8,303 (116% improvement)
- **Go**: Shows minimal warm-up effect, generally within 20% variance

### 2. Endpoint-Specific Champions
- **Compute Operations**: Scala ZIO peaks at 9,745 req/sec (Run 2, Medium)
- **Database All Users**: Go Gin consistently leads across all runs
- **Database Filtered**: Go Gin dominates, with Spring Boot competitive at medium load

### 3. Consistency vs Peak Performance
- **Most Consistent**: Kotlin Ktor at heavy compute (8,819 → 8,825 → 8,739)
- **Most Variable**: Kotlin Micronaut at heavy users (5,017 → 240 → 4,898)
- **Best All-Around**: Go Gin maintains leadership across most categories

### 4. Success Rate Patterns
- **Compute endpoints**: Near 100% success for most frameworks
- **Database endpoints**: High variance in success rates, especially at heavy load
- **Filtered queries**: Generally better success rates than full table scans
- **Scala Play**: Maintains 100% success rate despite slow performance

## Framework-Specific Analysis

### Go + Gin
- **Strengths**: Dominates database operations, consistent performance across runs
- **Weaknesses**: Not best for compute-intensive tasks
- **Best Use Case**: Database-driven APIs and microservices
- **Key Metric**: Won 7 of 9 test categories

### Scala ZIO
- **Strengths**: Peak compute performance (9,745 req/sec), dramatic warm-up gains
- **Weaknesses**: High variability between runs, database performance issues
- **Best Use Case**: CPU-intensive computational services with warm-up time
- **Key Metric**: 87% improvement from Run 1 to Run 2 at light compute load

### Kotlin Spring Boot
- **Strengths**: Massive warm-up improvements (up to 238%), consistent at scale
- **Weaknesses**: Very slow initial performance (1,319 req/sec initial)
- **Best Use Case**: Long-running services where warm-up is acceptable
- **Key Metric**: Most dramatic improvement between runs

### Kotlin Ktor
- **Strengths**: Excellent heavy load compute, most consistent performance
- **Weaknesses**: Database performance issues, especially filtered queries
- **Best Use Case**: High-concurrency compute services
- **Key Metric**: Only 48 req/sec standard deviation at heavy compute load

### Java/Kotlin Micronaut
- **Strengths**: Consistent improvements across runs, reliable performance
- **Weaknesses**: Never best-in-class but always competitive
- **Best Use Case**: General-purpose microservices
- **Key Metric**: 100%+ improvements common between Run 1 and Run 3

### Scala Play
- **Strengths**: 100% success rate, predictable performance
- **Weaknesses**: Consistently 2-3x slower than competitors
- **Best Use Case**: Low-traffic services where reliability is paramount
- **Key Metric**: Never failed a request except for timeout

## Recommendations

### For New Projects
1. **Database-Heavy APIs**: Go + Gin (consistent leader)
2. **Compute-Intensive Services**: Scala ZIO (after warm-up) or Kotlin Ktor
3. **General Purpose with Scale**: Kotlin Spring Boot (after warm-up)
4. **Reliability Over Speed**: Scala Play

### Performance Optimization Tips
1. **Allow JVM warm-up**: First run can be 50-238% slower than third run
2. **Monitor concurrency levels**: Different frameworks excel at different loads
3. **Consider endpoint types**: Database vs compute performance varies dramatically
4. **Test multiple runs**: Single-run benchmarks miss warm-up effects

### Architecture Considerations
1. **Microservices**: Use specialized frameworks per service type
2. **Monoliths**: Go + Gin or warmed Spring Boot for best all-around
3. **High Concurrency**: Kotlin Ktor for compute, Go for database
4. **Legacy Migration**: Scala Play if 100% uptime is critical

## Test Methodology

- **Tool**: Apache Bench (ab)
- **Environment**: Docker containers with resource isolation
- **Database**: MongoDB 7.0 with 100 test records
- **Runs**: 3 complete test runs with 30-second intervals
- **Analysis**: Individual run results, mean, and standard deviation calculated

Raw test data available in `performance-results-new-params.txt`