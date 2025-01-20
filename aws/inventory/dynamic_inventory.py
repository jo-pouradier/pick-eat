#!/usr/bin/env python
import os
import json

# Fetch environment variables
pick_eat_compose = os.getenv("AWS_PICK_EAT_COMPOSE")
if not pick_eat_compose:
    raise ValueError("No AWS_PICK_EAT_COMPOSE env")
pick_eat_ai = os.getenv("AWS_PICK_EAT_AI")
if not pick_eat_ai:
    raise ValueError("No AWS_PICK_EAT_AI env")
ssh_private_key_file = os.getenv("AWS_SSH_PRIVATE_KEY_FILE")
if not ssh_private_key_file:
    raise ValueError("No AWS_SSH_PRIVATE_KEY_FILE env")
docker_registry_username = os.getenv("GITLAB_USERNAME")
if not docker_registry_username:
    raise ValueError("No GITLAB_USERNAME env")
docker_registry_password = os.getenv("GITLAB_TOKEN")
if not docker_registry_password:
    raise ValueError("No GITLAB_TOKEN env")
pick_eat_private_ip = os.getenv("AWS_PICK_EAT_COMPOSE_PRIVATE_IP")
if not pick_eat_private_ip:
    raise ValueError("No AWS_PICK_EAT_COMPOSE_PRIVATE_IP env")

inventory = {
    "pick_eat_compose": {
        "hosts": [pick_eat_compose],
        "vars": {
            "docker_registry_username": docker_registry_username,
            "docker_registry_password": docker_registry_password,
        }
    },
    "pick_eat_ai": {
        "hosts": [pick_eat_ai],
        "vars": {
            "docker_registry_username": docker_registry_username,
            "docker_registry_password": docker_registry_password,
            "broker_host": pick_eat_private_ip,
        }
    },
    "docker_hosts": {
        "hosts": [pick_eat_ai, pick_eat_compose],
    },
    "all":{
        "vars":{
            "ansible_user": "ubuntu",
            "ansible_ssh_private_key_file": ssh_private_key_file,
            "ansible_ssh_common_args": "-o 'IdentitiesOnly=yes'",
            "ansible_python_interpreter": "/usr/bin/python3"
        }
    }
}

# Output the inventory as JSON
print(json.dumps(inventory, indent=2))
