#! /bin/sh

# setting up arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# verifying number of arguments
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# host info variables
hostname=$(hostname -f)
cpu_number=$(lscpu  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(lscpu | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(lscpu  | egrep "^Model\sname:" | awk '{print $3,$4,$5,$6,$7}' | xargs)
cpu_mhz=$(lscpu  | egrep "^Model\sname:" | awk '{print $7=$7*1000}' | xargs)
l2_cache=$(lscpu | egrep "^L2\scache:" | awk '{print $3}' | xargs)
total_mem=$(vmstat --unit M | tail -1 | awk '{print $4}')
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

# queries
insert_stmt="INSERT INTO host_info(hostname,  cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, timestamp, total_mem) VALUES('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', '$timestamp', '$total_mem');"

export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?