#!/bin/sh

set -e

MAIN_JAR_FILE=gazeplay-${project.version}.jar

export JAVA_OPTS="-Xms256m -Xmx1g"

WORKING_DIR=$(pwd)

echo "WORKING_DIR = ${WORKING_DIR}"

SCRIPT_DIR=$(dirname $0)

echo "SCRIPT_DIR = ${SCRIPT_DIR}"

LIB_DIR=${SCRIPT_DIR}/../lib

echo "LIB_DIR = ${LIB_DIR}"

export JAVA_CMD="java ${JAVA_OPTS} -jar ${LIB_DIR}/${MAIN_JAR_FILE}" 

echo "Executing command line: $JAVA_CMD"

${JAVA_CMD}
