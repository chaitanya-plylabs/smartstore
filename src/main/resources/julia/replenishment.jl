using Pkg
Pkg.add("JuMP")
Pkg.add("Cbc")
Pkg.add("JSON")
Pkg.add("Mux")

using JuMP
using Cbc
using JSON
using Mux

function replenish(requestJson)
    request = JSON.parse(requestJson)
    djrp_output = djrp_optimal(
        request["numberOfItems"],
        request["numberOfTimePeriods"],
        vcat([x' for x in request["orderingCost"]]...),
        vcat([x' for x in request["itemOrderingCost"]]...),
        vcat([x' for x in request["itemHoldingCost"]]...),
        vcat([x' for x in request["itemDemand"]]...),
        vcat([x' for x in request["itemReplenishmentCapacity"]]...),
        request["totalReplenishmentCapacity"],
        request["maxReplenishmentCycles"])
    result = Dict("objectiveValue" => djrp_output[1], "replenishmentFlags" => djrp_output[2], "replenishmentQuantities" => [djrp_output[3][i, :] for i in 1:size(djrp_output[3], 1)])
    return JSON.json(result);
end

function djrp_optimal(
    number_of_items,
    number_of_time_periods,
    ordering_cost,
    item_ordering_cost,
    item_holding_cost,
    item_demand,
    item_replenishment_capacity,
    total_replenishment_capacity,
    max_replenishment_cycles
    )
    n = copy(number_of_items)
    T = copy(number_of_time_periods)
    S = copy(ordering_cost)
    s = copy(item_ordering_cost)
    h = copy(item_holding_cost)
    d = copy(item_demand)
    M = sum(d[:,t] for t = 1:T)
    Cir = copy(item_replenishment_capacity)
    Ct = copy(total_replenishment_capacity)
    Rc = copy(max_replenishment_cycles)
    replenishmentModel = Model(with_optimizer(Cbc.Optimizer))
    @variable(replenishmentModel, z[1:T], Bin)
    @variable(replenishmentModel, I[1:n, 1:T], Int)
    @variable(replenishmentModel, y[1:n, 1:T], Bin)
    @variable(replenishmentModel, x[1:n, 1:T], Int)
    @objective(replenishmentModel, Min, sum(S[t]*z[t] + sum(s[i,t]*y[i,t] + h[i,t]*I[i,t] for i = 1:n) for t = 1:T))
    @constraint(replenishmentModel, [i=1:n], x[i,1] - I[i,1] == d[i,1])
    @constraint(replenishmentModel, [i=1:n, t=2:T], I[i,t-1] + x[i,t] - I[i,t] == d[i,t])
    @constraint(replenishmentModel, [i=1:n, t=1:T], x[i,t] <= M[i]*y[i,t])
    @constraint(replenishmentModel, [t=1:T], sum(y[i,t] for i = 1:n) <= n*z[t])
    @constraint(replenishmentModel, [i=1:n, t=1:T], x[i,t] <= Cir[i])
    @constraint(replenishmentModel, [t=1:T], sum(x[i,t] for i = 1:n) <= Ct)
    @constraint(replenishmentModel, [i=1:n, t=1:T], I[i,t] >= 0)
    @constraint(replenishmentModel, [i=1:n, t=1:T], x[i,t] >= 0)
    @constraint(replenishmentModel, sum(z[t] for t = 1:T) <= Rc)
    optimize!(replenishmentModel)
    rz = round.(Integer, value.(z));
    rx = round.(Integer, value.(x));
    return objective_value(replenishmentModel), rz, rx
end;



@app api = (
    Mux.defaults,
    page(respond("Nothing to see here...")),
    route("/replenish", req -> begin
       obj = String(req[:data])
       result = replenish(obj)
       Dict(:body => String(result),
            :headers => Dict("Content-Type" => "application/json")
           )
    end),
    Mux.notfound()
)
serve(api, 7000)
Base.JLOptions().isinteractive==0 && wait()