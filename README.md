# ABstract
An scaffold project for AB testing 


## TestContainer config
if use colima as test container, you need add the following system environment variables:
```shell
export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/Users/peichao.dong/.colima/docker.sock
export DOCKER_HOST=unix:///Users/peichao.dong/.colima/docker.sock
export TESTCONTAINERS_RYUK_DISABLED=true
```
add them to ~/.bash_profile and add add the follow config to the top of ~/.zshrc
```shell
if [ -f ~/.bash_profile ]; then
   source ~/.bash_profile
fi
```