use strict;
use LWP::Simple;
use JSON::Parse 'parse_json';

# Drop artifact in the current folder if present
system("rm -f wikiextractor_latest.jar");

my $api_url = "http://192.168.1.8:8081/service/rest/v1/search/assets?maven.artifactId=wikiextractor_2.12";
my $content = get($api_url);

die "Can't GET $api_url" if (! defined $content);

# Process JSON result
my @artifacts = ();
my $json = parse_json ($content);
foreach my $key ( keys %{ $json } ){
  my $arr = ${$json}{$key};
  foreach my $e (@$arr) {
    my $url = ${$e}{"downloadUrl"};
    if ($url =~ /wikiextractor_([0-9\.-]*)+\.jar$/ig){
      push @artifacts, $url;
    }
  }
}

my @sorted_artifacts = sort @artifacts;
my $last_artifact = pop @sorted_artifacts;

# Download latest artifact
print "Try to download latest artifact: $last_artifact";
system("curl $last_artifact -o wikiextractor_latest.jar")