use strict;
use LWP::Simple;

my $url = "http://192.168.1.8:8081/service/rest/v1/search/assets?maven.artifactId=wikiextractor_2.12";
my $content = get($url);

die "Can't GET $url" if (! defined $content);