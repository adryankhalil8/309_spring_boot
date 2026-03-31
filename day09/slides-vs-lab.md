# Day 9 Comparison: Slides vs Lab

This file compares what the Day 9 slides teach with what the Day 9 lab actually expects you to change in the project.

The goal is to answer:
- What comes from the slides as background?
- What is actually required to finish the lab?
- What files should change?
- What should stay alone?

---

## Big Picture

The slides cover three layers:
- security concepts
- a basic Spring Security setup
- an expanded role-based version

The lab only asks you to implement the practical parts needed for the car dealership API:
- add Spring Security
- create `SecurityConfig`
- make GET endpoints public
- require login for POST and PUT
- require `ADMIN` for DELETE
- test the request flow

So the lab is narrower than the slides. Not everything in the slides becomes a code change.

---

## What The Slides Add That The Lab Uses

These slide topics directly turn into lab work:

1. Add `spring-boot-starter-security`
- Slide section: `Adding Spring Security`
- Lab Part 1: `Add the Dependency`
- Project note: this dependency is already present in `project/cardealership/pom.xml`, so this part may already be done in your repo.

2. Create `SecurityConfig`
- Slide section: `Security Configuration`
- Lab Part 2: `Create Security Configuration`
- This is the main code change for Day 9.

3. Use HTTP Basic authentication
- Slide section: `HTTP Basic Auth`
- Lab Part 2 and Part 3 use it directly.

4. Use in-memory users
- Slide section: `In-Memory Users`
- Lab Part 2 expects `user` and `admin`.

5. Use BCrypt password encoding
- Slide section: `Password Encoding`
- Lab deliverables require BCrypt.

6. Apply authorization rules by HTTP method
- Slide section: `Authorization Rules`
- Lab Parts 2 and 4 are based on this.

---

## What The Slides Mention But The Lab Does Not Require You To Build

These are useful ideas from the slides, but they are not core coding tasks for this lab:

1. Authentication vs authorization explanation
- Important conceptually
- No direct file change required

2. JWT overview
- The slides explain JWT only as a concept
- The lab does not ask you to implement JWT

3. Custom 401/403 handling in `GlobalExceptionHandler`
- Mentioned in the slides
- The lab deliverables do not require adding these handlers
- You can leave this alone unless your instructor specifically wants custom security error responses

4. The “everything locked down first” demo state
- The slides show that adding the dependency causes 401 on everything
- The lab uses that as a quick check, but you do not keep the app in that state

---

## Most Important Difference Between Slides And Lab

The slides show two authorization setups:

### Slide setup A: simple authenticated writes
```java
.requestMatchers(HttpMethod.GET, "/api/cars/**").permitAll()
.requestMatchers(HttpMethod.GET, "/api/owners/**").permitAll()
.requestMatchers(HttpMethod.POST, "/api/**").authenticated()
.requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
.requestMatchers(HttpMethod.DELETE, "/api/**").authenticated()
```

### Slide setup B: stricter admin-only writes
```java
.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
.requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
.requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
```

### Lab expectation
The lab wants a middle version:
- `GET` is public
- `POST` requires authentication
- `PUT` requires authentication
- `DELETE` requires `ADMIN`

So do not copy the final role-based example from the slides exactly.

For the lab, your target rule set is:
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
    .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
    .requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
    .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
    .anyRequest().authenticated()
)
```

That is the most important clarification for Day 9.

---

## Specific Project Changes Needed

## 1. `project/cardealership/pom.xml`

### What the slides say
Add:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### What the lab needs
The same dependency.

### What your repo shows now
`spring-boot-starter-security` is already in `project/cardealership/pom.xml`.

### Action
- If it stays there, no new change is needed.
- Do not add it a second time.

---

## 2. Create `SecurityConfig.java`

### File to add
`project/cardealership/src/main/java/com/example/cardealership/config/SecurityConfig.java`

### Why this file matters
This is the main Day 9 implementation file. Most of the lab is really about adding this class correctly.

### What the slides provide
The slides give you a full starter version of this class.

### What the lab wants you to keep from the slides
- `@Configuration`
- `@EnableWebSecurity`
- `SecurityFilterChain`
- `UserDetailsService`
- `PasswordEncoder`
- `httpBasic(withDefaults())`
- `BCryptPasswordEncoder`
- `InMemoryUserDetailsManager`

### What to customize for the lab
Use the lab’s authorization rules, not the strictest slide version.

### Recommended class shape for the lab
```java
package com.example.cardealership.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## 3. Existing controllers, services, repositories, and entities

### What the slides might make you think
Security might require changing controller methods or adding annotations everywhere.

### What the lab actually requires
For this lab, the security rules live in `SecurityConfig`.
That means:
- you usually do not need to change controller code
- you do not need to edit service logic
- you do not need to edit entity classes
- you do not need to edit repositories

### Action
Leave these alone unless something in your project is broken for a separate reason.

---

## 4. Global exception handling

### What the slides show
Possible `AccessDeniedException` handling in `GlobalExceptionHandler`

### What the lab requires
Not required by the deliverables list.

### Action
Leave `GlobalExceptionHandler` unchanged unless:
- your instructor wants custom JSON for `403`
- you are explicitly told to handle security exceptions there

---

## 5. Testing expectations from the lab

The slides explain how to test auth manually. The lab turns that into a checklist.

### Tests that must pass for the lab
1. `GET /api/cars` with no auth -> `200`
2. `POST /api/cars` with no auth -> `401`
3. `POST /api/cars` with `user:password` -> `201`
4. `PUT /api/cars/1` with `user:password` -> `200`
5. `DELETE /api/cars/1` with `user:password` -> `403`
6. `DELETE /api/cars/1` with `admin:admin123` -> `204`
7. `GET /api/cars/1` after delete -> `404`

### Meaning of failures
- `401` means not logged in or wrong credentials
- `403` means logged in but wrong role

---

## What You Actually Need To Change For Day 9

If you want the shortest possible implementation checklist, it is this:

1. Confirm `spring-boot-starter-security` exists in `project/cardealership/pom.xml`
2. Create `config/SecurityConfig.java`
3. Add:
- stateless sessions
- CSRF disabled
- HTTP Basic auth
- in-memory `user` and `admin`
- BCrypt password encoding
4. Set rules so:
- all `GET /api/**` are public
- `POST /api/**` requires login
- `PUT /api/**` requires login
- `DELETE /api/**` requires `ADMIN`
5. Restart and test the 7 lab scenarios

---

## What You Do Not Need To Change For Day 9

- `CarController.java`
- `OwnerController.java`
- service interfaces
- service implementations
- repositories
- DTOs
- entity classes
- mappers
- seed data, unless you want more sample records for testing
- JWT-related files

---

## Common Mistakes To Avoid

1. Copying the slides’ strict admin-only config for POST and PUT
- The lab does not want that
- POST and PUT should allow any authenticated user

2. Forgetting `.httpBasic(withDefaults())`
- Without this, Basic Auth testing will not work as expected

3. Forgetting the password encoder
- The in-memory users still need encoded passwords

4. Thinking you must edit every controller
- For this lab, one security config class does the real work

5. Adding custom exception handlers before the base security flow works
- Get the lab passing first

---

## Final Mapping

| Slides Topic | Lab Relevance | Action |
|---|---|---|
| Authentication vs authorization | Background | Understand only |
| Add security dependency | Required | Confirm in `pom.xml` |
| SecurityFilterChain | Required | Implement in `SecurityConfig.java` |
| In-memory users | Required | Add `user` and `admin` |
| BCrypt encoder | Required | Add `PasswordEncoder` bean |
| Public GET endpoints | Required | Configure in filter chain |
| Authenticated POST/PUT | Required | Configure in filter chain |
| ADMIN-only DELETE | Required | Configure in filter chain |
| Custom 401/403 handler | Optional | Usually skip for this lab |
| JWT overview | Concept only | Do not implement |

---

## Bottom Line

The Day 9 slides are broader than the lab, but the actual code changes are small.

In this project, Day 9 is mostly:
- one dependency check
- one new config class
- one set of authorization rules
- manual endpoint testing

That is the main clarification the comparison is meant to provide.
