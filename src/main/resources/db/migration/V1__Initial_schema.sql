CREATE TABLE franchise (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,

    CONSTRAINT ctr_franchises_name UNIQUE (name)
);

CREATE TABLE branch (
    id BIGSERIAL PRIMARY KEY,
    franchise_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,

    CONSTRAINT fk_branches_franchise FOREIGN KEY (franchise_id)
        REFERENCES franchise(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT ctr_branches_franchise_name UNIQUE (franchise_id, name)
);

CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,

    CONSTRAINT fk_products_branch FOREIGN KEY (branch_id)
        REFERENCES branch(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT uk_products_branch_name UNIQUE (branch_id, name),
    CONSTRAINT ctr_products_name_not_empty CHECK (TRIM(name) <> ''),
    CONSTRAINT chk_products_stock_non_negative CHECK (stock >= 0)
);