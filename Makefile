.DEFAULT_GOAL := help
.ONESHELL: 

mkfile_path := $(abspath $(lastword $(MAKEFILE_LIST)))
mkfile_dir := $(dir $(mkfile_path))

# Begin OS detection
ifeq ($(OS),Windows_NT) # is Windows_NT on XP, 2000, 7, Vista, 10...
    export OPERATING_SYSTEM := Windows
else
    export OPERATING_SYSTEM := $(shell uname)  # same as "uname -s"
endif

# Override the container tool. Tries docker first and then tries podman.
export CONTAINER_TOOL ?= auto
ifeq ($(CONTAINER_TOOL),auto)
	override CONTAINER_TOOL = $(shell docker version >/dev/null 2>&1 && echo docker || echo podman)
endif
# If we're using podman create pods else if we're using docker create networks.
export CURRENT_DIR = $(shell pwd)

FORMATTING_BEGIN_YELLOW = \033[0;33m
FORMATTING_BEGIN_BLUE = \033[36m
FORMATTING_END = \033[0m

# "One weird trick!" https://www.gnu.org/software/make/manual/make.html#Syntax-of-Functions
EMPTY:=
SPACE:= ${EMPTY} ${EMPTY}
COMMA:= ,

help:
	@awk 'BEGIN {FS = ":.*##"; printf "Usage: make ${FORMATTING_BEGIN_BLUE}<target>${FORMATTING_END}\nSelected container tool: ${FORMATTING_BEGIN_BLUE}${CONTAINER_TOOL}${FORMATTING_END}\n"} /^[a-zA-Z0-9_-]+:.*?##/ { printf "  ${FORMATTING_BEGIN_BLUE}%-46s${FORMATTING_END} %s\n", $$1, $$2 } /^##@/ { printf "\n\033[1m%s\033[0m\n", substr($$0, 5) } ' $(MAKEFILE_LIST)

.PHONY: build-backend
build-backend: ## Builds and compiles backend
	cd ./backend
	./mvnw compile

.PHONY: up
up: ## Runs the elastic search container with kibana
	$(CONTAINER_TOOL)-compose -f docker-compose.yaml up

.PHONY: down
down: ## Removes the containers
	$(CONTAINER_TOOL)-compose -f docker-compose.yaml down
	
.PHONY: run
run: ## Run the backend
	cd ./backend
	./mvnw spring-boot:run