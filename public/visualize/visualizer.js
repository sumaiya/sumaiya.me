var width = 1425,
    height = 725,
    centered;

var color = d3.scale.category20();

var force = d3.layout.force()
    .charge(-120)
    .linkDistance(function(d) {return d.value;})
    .size([width, height]);

var svg = d3.select("body").append("svg")
    .attr("width", width)
    .attr("height", height);

d3.json("restaurantData.json", function(error, graph) {
  force
      .nodes(graph.nodes)
      .links(graph.links)
      .start();


  var link = svg.selectAll(".link")
      .data(graph.links)
      .enter().append("line")
      .attr("class", "link")

  var node = svg.selectAll(".node")
      .data(graph.nodes)
      .enter().append("circle")
      .attr("class", "node")
      .attr("r", function(d) {
      if (d.radius <= 10) {return d.radius} 
      else {return d.radius}
      ;})
      .attr("u", function(d) {return d.url;})
      .style("fill", function(d) { return color(d.group); })
      .on("click", function(d,i) {window.open(d.url); })

      .call(force.drag);

  node.append("title")
        .text(function(d) {
        if (d.name == "You are here") {return d.name} 
        else if (d.radius == 5) 
        {return d.name + "\n" + (d.radius/5) + " star"} 
        else {return d.name + "\n" + (d.radius/5) + " stars"}; });



  force.on("tick", function() {
    link.attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });

    node.attr("cx", function(d) { return d.x; })
        .attr("cy", function(d) { return d.y; });
  });
});
