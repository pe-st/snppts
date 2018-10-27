# Dropwizard Metrices Extensions

## SlidingTimeWindowMeter

How can I reset a Dropwizard Metric after reporting to Kibana?

Unfortunately there seems to be no proper/built-in solution. I have found the following workarounds:

- Use a custom `Gauge` and reset the value in the getValue() method. Very ugly and loses the idempotence and immutability of a read-only method.
- Use a `Meter` and display the one-minute-rate. The one-minute-rate is not a sharp moving average over one minute though, but an exponentially-weighted moving average, i.e. it doesn't drop immediately to zero when there were no transactions/requests/increments in a one minute window.
- Use a `Histogram` with a `SlidingTimeWindowReservoir`. The disadvantage here is that the metric needs to keep in memory all the counts of the chosen time window.
- (My preferred solution) Use a custom Meter that calculates a real moving average over time instead of the exponentially-weighted one. Enter `SlidingTimeWindowMeter`: as it uses the same interface as the Dropwizard Meter, it's confined to moving averages over one, five or fifteen minutes, but for my use case the one-minute-rate is what I want.