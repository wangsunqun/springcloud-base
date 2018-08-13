local result = redis.call('incrby',KEYS[1],ARGV[1]);
if(result==tonumber(ARGV[1]))
then
	redis.call('expire',KEYS[1],tonumber(ARGV[2]));
end
return result;

