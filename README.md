# bb-slack

send slack message with babashka, wrapper of this endpoint
https://api.slack.com/methods/chat.postMessage

need babashka, quick install:
```
bash < <(curl -s https://raw.githubusercontent.com/babashka/babashka/master/install)

```
## quick run
you can run the jar immediately using babashka

download the [jar](https://github.com/keychera/bb-slack/releases/tag/latest)

then run
```sh
bb slack.jar
:slack/text "hello slack from commandline babashka quick run" \
:slack/token "<token-here>" \
:slack/channel-id "<channel-id-here>"
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

# only text
bb -m main --slack/text "hello slack from commandline babashka"

# with template
bb -m main --slack/template test/block.json

# with template and context-edn
bb -m main --slack/template test/block.json --slack/context-edn test/context.edn

# with template and context-str
bb -m main --slack/template test/block.json --slack/context-str '{:title "This is from commandline" :from "someone"}'

# with thread-ts
bb -m main --slack/text "hello slack thread replies from babashka" --slack/thread-ts 1690280713.589219

# with reply-broadcast
bb -m main --slack/text "hello broadcasted thread replies from babashka" --slack/thread-ts 1690280713.589219 --slack/reply-broadcast
```


### release

```sh
bb uberjar slack.jar -m main
bb slack.jar --slack/text "hello slack from commandline babashka"
```

note:
  - build template.json with https://app.slack.com/block-kit-builder
  - context-edn is rendered using Selmer rules (https://github.com/yogthos/Selmer)