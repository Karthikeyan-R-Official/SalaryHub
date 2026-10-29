BEGIN;

-- Generate 10,000 fictional employees.
-- SEED- employee codes make these records easy to identify.
-- ON CONFLICT makes the script safe to run again.

WITH source AS (
    SELECT
        i,
        ARRAY[
            'Arun', 'Priya', 'Rahul', 'Sneha', 'Kiran',
            'Ananya', 'Vikram', 'Divya', 'Rohan', 'Meera',
            'Amit', 'Neha', 'Sanjay', 'Kavya', 'Aditya',
            'Pooja', 'Vijay', 'Ishita', 'Nikhil', 'Asha'
        ] AS first_names,

        ARRAY[
            'Kumar', 'Sharma', 'Patel', 'Reddy', 'Singh',
            'Gupta', 'Rao', 'Iyer', 'Das', 'Nair',
            'Mehta', 'Joshi', 'Verma', 'Shah', 'Mishra'
        ] AS last_names,

        ARRAY[
            'India', 'USA', 'UK',
            'Germany', 'Singapore', 'Australia'
        ] AS countries,

        ARRAY[
            'Engineering', 'Human Resources', 'Finance',
            'Operations', 'Sales', 'Marketing',
            'Customer Support', 'Legal'
        ] AS departments,

        ARRAY[
            'Software Engineer', 'Senior Software Engineer',
            'Business Analyst', 'Data Analyst',
            'HR Specialist', 'Finance Analyst',
            'Project Manager', 'QA Engineer',
            'Support Engineer', 'Team Lead'
        ] AS job_titles

    FROM generate_series(1, 10000) AS series(i)
)

INSERT INTO employees (
    employee_code,
    first_name,
    last_name,
    email,
    country,
    department,
    job_title,
    hire_date,
    employment_status,
    created_at,
    updated_at
)
SELECT
    'SEED-' || lpad(i::text, 5, '0'),

    first_names[1 + ((i - 1) % array_length(first_names, 1))],

    last_names[1 + ((i - 1) % array_length(last_names, 1))],

    lower(
        first_names[1 + ((i - 1) % array_length(first_names, 1))]
        || '.'
        || last_names[1 + ((i - 1) % array_length(last_names, 1))]
        || i::text
        || '@example.com'
    ),

    countries[1 + ((i - 1) % array_length(countries, 1))],

    departments[1 + ((i - 1) % array_length(departments, 1))],

    job_titles[1 + ((i - 1) % array_length(job_titles, 1))],

    DATE '2015-01-01' + ((i * 13) % 3650),

    CASE
        WHEN i % 20 = 0 THEN 'INACTIVE'
        ELSE 'ACTIVE'
    END,

    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP

FROM source

ON CONFLICT (employee_code) DO NOTHING;


-- Create one fictional salary record for each seeded employee.
-- Currency depends on the employee's country.

INSERT INTO salary_records (
    employee_id,
    amount,
    currency,
    effective_date,
    notes,
    created_at
)
SELECT
    e.id,

    (
        45000
        + (
            (
                substring(e.employee_code FROM 6)::integer * 137
            ) % 100000
        )
    )::numeric(19, 2),

    CASE e.country
        WHEN 'India' THEN 'INR'
        WHEN 'USA' THEN 'USD'
        WHEN 'UK' THEN 'GBP'
        WHEN 'Germany' THEN 'EUR'
        WHEN 'Singapore' THEN 'SGD'
        WHEN 'Australia' THEN 'AUD'
    END,

    DATE '2025-01-01'
        + (
            (
                substring(e.employee_code FROM 6)::integer * 7
            ) % 365
        ),

    'Fictional sample salary for demonstration',

    CURRENT_TIMESTAMP

FROM employees e

WHERE e.employee_code LIKE 'SEED-%'

ON CONFLICT (employee_id, effective_date) DO NOTHING;

COMMIT;