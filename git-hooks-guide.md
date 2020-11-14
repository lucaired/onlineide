# Git commit message prefix guide
To automatically prefix all commit messages with the branch number.
With the hook all commit messages on branch `1-initial-setup` will be prefixed with `[1]`.
 

Locally, in our onlineide directory, create and add file `.git/hooks/pre-commit-msg` with the following content:
```
#!/bin/bash
FILE=$1
MESSAGE=$(cat $FILE)
TICKET=[$(git rev-parse --abbrev-ref HEAD | grep -Eo '^(\w+/)?(\w+[-_])?[0-9]+' | grep -Eo '(\w+[-])?[0-9]+' | tr "[:lower:]" "[:upper:]")]
if [[ $TICKET == "[]" || "$MESSAGE" == "$TICKET"* ]];then
  exit 0;
fi

echo "$TICKET $MESSAGE" > $FILE
```

Finally, make the newly created hook executable by running `chmod +x .git/hooks/prepare-commit-msg`.


