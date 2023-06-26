# bb-slack

send slack message with babashka

need babashka, quick install:
```
bash < <(curl -s https://raw.githubusercontent.com/babashka/babashka/master/install)
```

## how to run

set up SLACK_TOKEN and CHANNEL_ID in the environment variable

e.g. below is in the file `env.sh`

```sh
export SLACK_TOKEN=<token-here>
export CHANNEL_ID=<channel-id-here>
```
then
```sh
chmod +x env.sh
source ./env.sh

bb -m main :text "hello slack from commandline babashka 2"
```