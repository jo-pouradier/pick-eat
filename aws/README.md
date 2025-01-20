# AWS

## EC2

2 EC2 instances (pick_eat_compose, pick_eat_ai)
 - use ubuntu 24.04LTS
 - use t2.medium with 30GB of storage
 - with the same key pair (store it well!!)
 - security group: enable SSH, HTTP, HTTPS, 61613
 - becarefull on region

Create a standalone volume with 15 go in same region
 - attach volume to the first instance (pick_eat_compose)

## ENV

Create a gitlab token with access to docker registry = `GITLAB_TOKEN`
Add your gitlab username = `GITLAB_USERNAME`

Add your instances url (something like ec2-(ip).compute-1.amazonaws.com) = `AWS_PICK_EAT_COMPOSE` and `AWS_PICK_EAT_AI`
Add `AWS_PICK_EAT_COMPOSE` private IP (in the VPC)
Add you key pair path = `AWS_SSH_PRIVATE_KEY_FILE`

## Command

```bash
source .env
python -m pip install -r requirements.txt
python -m ansible-playbook mount-files.yaml
pyhton -m ansible-playbook deploy-docker.yaml
python -m ansible-playbook deploy-app.yml
```
