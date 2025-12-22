# Architecture Guidelines

These guidelines define the **module structure, dependency rules, and decision principles** for this app.

The goal is **clarity over purity**: a simple structure that is easy to understand, maintain, and evolve without premature abstraction.

---

## 1. Top-level Structure

The project is organized into **two umbrellas only**:

```
platform/
features/
```

This is intentional. Additional umbrellas should not be introduced unless there is clear, sustained pressure to do so.

---

## 2. Platform

### Purpose

`platform` contains **shared capabilities** that are not user-facing and have no feature semantics.

Think:

> "How the app talks to the world or renders UI consistently"

### What belongs in `platform`

* Network clients (API definitions + implementations)
* Storage (databases, preferences, caches)
* Auth / security utilities
* Analytics
* Design system (themes, tokens, reusable UI components)

Example:

```
platform/
 ├── network/
 │   ├── api/
 │   └── impl/
 ├── storage/
 └── designsystem/
```

### What does NOT belong in `platform`

* ViewModels
* Screens or navigation
* Feature-specific business logic
* Anything named after a business concept (cart, checkout, profile, etc.)

### Stability rule

Code in `platform` should:

* Change **slowly**
* Be reusable by many features
* Avoid knowing *why* it is used

---

## 3. Features

### Purpose

`features` contains **everything that exists because the user can do something**.

Features are **vertical slices**: UI + state + business logic live together.

### Feature structure

Each feature is split into **three explicit submodules**:

```
features/
 └── cart/
     ├── ui/
     ├── api/
     └── implementation/
```

#### Submodule responsibilities

* `ui`

  * Screens and composables
  * ViewModels and UI state
  * Navigation glue
  * Depends on `api` and `platform`

* `api`

  * Public contracts of the feature
  * Interfaces, models, and use cases exposed to UI
  * Contains **no implementations**
  * Stable boundary

* `implementation`

  * Concrete implementations of `api`
  * Repositories, business logic, orchestration
  * Koin / DI bindings
  * Depends on `platform`

This enforces a clear separation between **what the feature exposes** and **how it works internally**.

---

## 4. Repositories

### Rules

* Repositories are **feature-owned by default**
* Repositories may depend on `platform`
* Repositories must not be shared between features prematurely

Example:

```
platform/network/CartApi
features/cart/data/CartRepository
```

Even if two features call the same API, they may each have their own repository.

Duplication is acceptable until it becomes painful.

---

## 5. Sharing Logic Between Features

### Default stance

> Duplication is cheaper than the wrong abstraction.

Do **not** extract shared logic until:

* It is used by multiple features
* The logic is stable
* Changes cause real maintenance pain

### When extraction is justified

If logic becomes truly shared and generic, it may be promoted into `platform` as a **capability**.

Example:

```
platform/cartlogic/
```

This should be rare and deliberate.

---

## 6. Dependency Rules (Hard Rules)

```
features → platform
platform ✕ features
feature A ✕ feature B
```

* Features never depend on other features
* Platform never depends on features
* Cycles are not allowed

These rules should be enforceable via Gradle.

---

## 7. Design System

If present, the design system lives in `platform`:

```
platform/designsystem/
```

Rules:

* UI-only (colors, typography, components)
* No business logic
* No feature knowledge

Features may depend on the design system freely.

---

## 8. When to Change the Architecture

Only consider adding new umbrellas (e.g. shared business modules) if:

* The app grows significantly
* Multiple features share complex business rules
* Duplication becomes a measurable problem

Architecture should evolve **reactively**, not proactively.

---

## 9. Guiding Principles

* Prefer clarity over theoretical purity
* Name things after what they are, not architectural concepts
* Accept small duplication early
* Optimize for understanding by new contributors

If a structure needs a long explanation, it is probably wrong.

---

**This document is a living guideline, not a strict law.**
Changes should be driven by real pain, not speculation.
