-- =====================================================================
-- 🚀 SUPABASE POSTGRESQL SCHEMA FOR SMART BEAUTY FACEBOOK PAGE CRAWLER
-- =====================================================================
-- This script provisions the relative database schema, relationships,
-- indexes, automatic timestamp state management, and basic Row-Level
-- Security (RLS) policies for high-performance crawling and ranking queries.

-- Enable UUID extension if not already loaded
create extension if not exists "uuid-ossp";

-- ==========================================
-- 1. STORES TABLE
-- ==========================================
create table public.facebook_stores (
    id uuid primary key default gen_random_uuid(),
    name text not null,
    facebook_url text not null unique,
    district text not null default 'Dhaka',
    is_verified boolean not null default false,
    is_premium boolean not null default false,
    logo_text varchar(4), -- e.g. "GH" for Glow Haven
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-- Comment explanations
comment on table public.facebook_stores is 'Stores active cosmetic pages scraped or moderated on the platform.';
comment on column public.facebook_stores.facebook_url is 'Unique URL indicator used by the headless scraper loop to scrape feed streams.';

-- ==========================================
-- 2. PRODUCTS TABLE (Global Product Catalog)
-- ==========================================
create table public.products (
    id uuid primary key default gen_random_uuid(),
    brand_name text not null,
    product_name text not null,
    search_token text, -- concatenated lower-case string for optimized full-text matching
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    unique(brand_name, product_name)
);

comment on table public.products is 'Canonical repository directory of cosmetics brands and products.';

-- ==========================================
-- 3. STORE INVENTORY TABLE (Active Inventory Cache)
-- ==========================================
create table public.store_inventory (
    id uuid primary key default gen_random_uuid(),
    store_id uuid not null references public.facebook_stores(id) on delete cascade,
    product_id uuid not null references public.products(id) on delete cascade,
    availability_status text not null check (availability_status in ('Available', 'Coming Soon', 'Unavailable')),
    price_bdt integer not null default 0,
    restock_days integer not null default 0,
    
    -- Inventory Freshness Columns
    last_claimed_at timestamptz not null default now(), -- Tracks when a merchant or crawler last verified stock
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    
    -- Ensure stores only have one record per product catalog index
    unique(store_id, product_id)
);

comment on table public.store_inventory is 'Links active product stock lines to specific scrapable Facebook stores.';
comment on column public.store_inventory.availability_status is 'Fulfillment status: "Available", "Coming Soon", or "Unavailable".';
comment on column public.store_inventory.last_claimed_at is 'Highly critical timestamp tracking exactly when stock freshness was last claimed/scraped.';

-- ==========================================
-- 4. AUTO-UPDATE UPDATED_AT TIMESTAMP FUNCTIONS
-- ==========================================
create or replace function public.trigger_set_timestamp()
returns trigger as $$
begin
  new.updated_at = now();
  return new;
end;
$$ language plpgsql;

-- Apply triggers across tracking components
create trigger set_timestamp_facebook_stores
before update on public.facebook_stores
for each row execute function public.trigger_set_timestamp();

create trigger set_timestamp_products
before update on public.products
for each row execute function public.trigger_set_timestamp();

create trigger set_timestamp_store_inventory
before update on public.store_inventory
for each row execute function public.trigger_set_timestamp();

-- ==========================================
-- 5. PERFORMANCE INDEXES FOR HOME RATINGS & QUEUES
-- ==========================================
-- High performance directory index sorting and crawling lookups
create index idx_stores_district on public.facebook_stores(district);
create index idx_inventory_status on public.store_inventory(availability_status);
create index idx_inventory_freshness on public.store_inventory(last_claimed_at desc);
create index idx_inventory_store_prod on public.store_inventory(store_id, product_id);

-- ==========================================
-- 6. DYNAMIC STORE FULFILLMENT VIEWS (SQL Ranking Engine)
-- ==========================================
-- A pre-compiled PostgreSQL View that mirrors the 100% list matching logic inside our Android app let.
-- This rolls up availability, stock volume rankings, and claims freshness!
create or replace view public.view_store_fulfillment as
select 
    s.id as store_id,
    s.name as store_name,
    s.facebook_url,
    s.district,
    s.is_verified,
    s.is_premium,
    count(case when i.availability_status = 'Available' then 1 end) as total_available_items,
    max(i.last_claimed_at) as last_updated_claim
from public.facebook_stores s
left join public.store_inventory i on s.id = i.store_id
group by s.id, s.name, s.facebook_url, s.district, s.is_verified, s.is_premium;

-- ==========================================
-- 7. SUPABASE SECURITY & RLS SETUP (Row Level Security)
-- ==========================================
alter table public.facebook_stores enable row level security;
alter table public.products enable row level security;
alter table public.store_inventory enable row level security;

-- Create Security Policies (Public read, authenticated scraper / admin write)
create policy "Allow public read access to active stores"
    on public.facebook_stores for select
    using (true);

create policy "Allow public read access to global product catalog"
    on public.products for select
    using (true);

create policy "Allow public read access to live inventories"
    on public.store_inventory for select
    using (true);

create policy "Allow team scrapers to update stores"
    on public.facebook_stores for all
    to authenticated
    using (true)
    with check (true);

create policy "Allow team scrapers to insert products"
    on public.products for all
    to authenticated
    using (true)
    with check (true);

create policy "Allow team scrapers to manage inventory streams"
    on public.store_inventory for all
    to authenticated
    using (true)
    with check (true);
