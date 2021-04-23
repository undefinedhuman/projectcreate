usage() {
cat << EOF
usage: bash ./pack -o os_name
-o    | --os_name           (Required)            OS to pack (windows, macos, linux)
-h    | --help                                    Shows this info
EOF
}
os_name=
while [ "$1" != "" ]; do
    case $1 in
        -o | --os_name )
            shift
            os_name=$1
        ;;
        -h | --help )    usage
            exit
        ;;
        * )              usage
            exit 1
    esac
    shift
done

if [ -z "$os_name" ]; then
    echo "OS name is required, provide it with the flag: -o os_name"
    exit
fi

java -jar packr.jar config/config-"$os_name".json