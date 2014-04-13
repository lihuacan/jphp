--TEST--
Basic cursor test append
--FILE--
<?php

use php\util\Cursor;

$arr = [1,2];
$callback = function($e) { var_dump($e); };

echo "--simple\n";
Cursor::of($arr)
    ->append(['foo','bar'])
    ->each($callback);

echo "--with-result\n";
Cursor::of($arr)
    ->skip(1)
    ->append(['foo', 'bar'])
    ->each($callback);

?>
--EXPECT--
--simple
int(1)
int(2)
string(3) "foo"
string(3) "bar"
--with-result
int(2)
string(3) "foo"
string(3) "bar"